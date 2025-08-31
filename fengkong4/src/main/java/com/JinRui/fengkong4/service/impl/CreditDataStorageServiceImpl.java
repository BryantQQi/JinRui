package com.JinRui.fengkong4.service.impl;

import com.JinRui.fengkong4.entity.*;
import com.JinRui.fengkong4.entity.mongo.*;
import com.JinRui.fengkong4.parser.ParseException;
import com.JinRui.fengkong4.repository.*;
import com.JinRui.fengkong4.service.CreditDataStorageService;
import com.JinRui.fengkong4.service.CreditReportParseService;
import com.JinRui.fengkong4.service.TreeNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 征信数据存储服务实现类
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class CreditDataStorageServiceImpl implements CreditDataStorageService {

    @Autowired
    private TreeNodeService treeNodeService;

    @Autowired
    private CreditReportParseService creditReportParseService;

    @Autowired
    private CreditReportMongoRepository creditReportRepository;

    @Autowired
    private PersonalInfoMongoRepository personalInfoRepository;

    @Autowired
    private InfoSummaryMongoRepository infoSummaryRepository;

    @Autowired
    private CreditTransactionMongoRepository creditTransactionRepository;

    @Autowired
    private QueryRecordMongoRepository queryRecordRepository;

    @Autowired
    private PublicInfoMongoRepository publicInfoRepository;

    @Autowired
    private NonCreditTransactionMongoRepository nonCreditTransactionRepository;

    @Override
    @Transactional
    public String saveCreditDataByIdAndName(String idCard, String name) throws ParseException {
        log.info("开始保存征信数据，身份证：{}，姓名：{}", maskIdCard(idCard), name);

        try {
            // 参数验证
            if (!treeNodeService.validateIdCard(idCard)) {
                throw new ParseException("身份证号码格式不正确", "INVALID_ID_CARD", "VALIDATION");
            }

            if (!treeNodeService.validateName(name)) {
                throw new ParseException("姓名格式不正确", "INVALID_NAME", "VALIDATION");
            }

            // 检查是否已存在
            if (existsCreditData(idCard, name)) {
                log.warn("用户征信数据已存在，身份证：{}，姓名：{}", maskIdCard(idCard), name);
                return "用户征信数据已存在，无需重复保存";
            }

            // 调用征信解析服务直接获取CreditReport对象
            CreditReport creditReport = creditReportParseService.getCreditReportByIdAndName(idCard, name);

            // 生成唯一的征信报文ID
            String creditReportId = UUID.randomUUID().toString().replace("-", "");

            // 保存主表数据
            saveCreditReportMain(creditReport, creditReportId, idCard, name);

            // 保存子表数据
            savePersonalInfo(creditReport.getPersonalInfo(), creditReportId);
            saveInfoSummary(creditReport.getInfoSummary(), creditReportId);
            saveCreditTransactions(creditReport.getCreditTransactions(), creditReportId);
            saveQueryRecords(creditReport.getQueryRecords(), creditReportId);
            savePublicInfos(creditReport.getPublicInfos(), creditReportId);
            saveNonCreditTransactions(creditReport.getNonCreditTransactions(), creditReportId);

            log.info("征信数据保存完成，征信报文ID：{}", creditReportId);
            return "征信数据保存成功，征信报文ID：" + creditReportId;

        } catch (ParseException e) {
            log.error("保存征信数据失败", e);
            throw e;
        } catch (Exception e) {
            log.error("保存征信数据失败", e);
            throw new ParseException("保存征信数据失败：" + e.getMessage(), e, "SAVE_ERROR", "STORAGE");
        }
    }

    @Override
    public boolean existsCreditData(String idCard, String name) {
        return creditReportRepository.existsByIdNumberAndName(idCard, name);
    }

    @Override
    @Transactional
    public String deleteCreditDataByIdAndName(String idCard, String name) {
        log.info("开始删除征信数据，身份证：{}，姓名：{}", maskIdCard(idCard), name);

        try {
            List<CreditReportMongo> creditReports = creditReportRepository.findByIdNumberAndName(idCard, name);
            
            if (creditReports.isEmpty()) {
                return "未找到相关征信数据";
            }

            for (CreditReportMongo creditReport : creditReports) {
                String creditReportId = creditReport.getCreditReportId();
                
                // 删除子表数据
                personalInfoRepository.deleteByCreditReportId(creditReportId);
                infoSummaryRepository.deleteByCreditReportId(creditReportId);
                creditTransactionRepository.deleteByCreditReportId(creditReportId);
                queryRecordRepository.deleteByCreditReportId(creditReportId);
                publicInfoRepository.deleteByCreditReportId(creditReportId);
                nonCreditTransactionRepository.deleteByCreditReportId(creditReportId);
                
                // 删除主表数据
                creditReportRepository.delete(creditReport);
            }

            log.info("征信数据删除完成，共删除{}条记录", creditReports.size());
            return "征信数据删除成功，共删除" + creditReports.size() + "条记录";

        } catch (Exception e) {
            log.error("删除征信数据失败", e);
            throw new RuntimeException("删除征信数据失败：" + e.getMessage(), e);
        }
    }



    /**
     * 保存征信报文主表数据
     */
    private void saveCreditReportMain(CreditReport creditReport, String creditReportId, String idCard, String name) {
        CreditReportMongo creditReportMongo = new CreditReportMongo();
        creditReportMongo.setCreditReportId(creditReportId);
        creditReportMongo.setIdNumber(idCard);
        creditReportMongo.setName(name);
        
        if (creditReport.getHeader() != null) {
            ReportHeader header = creditReport.getHeader();
            creditReportMongo.setReportNumber(header.getReportNumber());
            creditReportMongo.setGenerateTime(header.getGenerateTime());
            creditReportMongo.setInstitutionCode(header.getInstitutionCode());
            creditReportMongo.setQueryReason(header.getQueryReason());
            creditReportMongo.setReportType(header.getReportType());
            creditReportMongo.setReportQueryTime(header.getReportQueryTime());
            creditReportMongo.setHasValidCreditInfo(header.getHasValidCreditInfo());
            creditReportMongo.setFirstCreditMonthDiff(header.getFirstCreditMonthDiff());
        }

        creditReportRepository.save(creditReportMongo);
        log.debug("保存征信报文主表数据成功");
    }

    /**
     * 保存个人信息数据
     */
    private void savePersonalInfo(PersonalInfo personalInfo, String creditReportId) {
        if (personalInfo == null) {
            return;
        }

        PersonalInfoMongo personalInfoMongo = new PersonalInfoMongo();
        personalInfoMongo.setCreditReportId(creditReportId);
        personalInfoMongo.setName(personalInfo.getName());
        personalInfoMongo.setIdType(personalInfo.getIdType());
        personalInfoMongo.setIdNumber(personalInfo.getIdNumber());
        personalInfoMongo.setGender(personalInfo.getGender());
        personalInfoMongo.setBirthDate(personalInfo.getBirthDate());
        personalInfoMongo.setMaritalStatus(personalInfo.getMaritalStatus());
        personalInfoMongo.setEducation(personalInfo.getEducation());
        personalInfoMongo.setDegree(personalInfo.getDegree());
        personalInfoMongo.setEmploymentStatus(personalInfo.getEmploymentStatus());
        personalInfoMongo.setNationality(personalInfo.getNationality());
        personalInfoMongo.setEmail(personalInfo.getEmail());
        personalInfoMongo.setAddress(personalInfo.getAddress());
        personalInfoMongo.setMobile(personalInfo.getMobile());
        personalInfoMongo.setCompanyName(personalInfo.getCompanyName());
        personalInfoMongo.setCompanyAddress(personalInfo.getCompanyAddress());
        personalInfoMongo.setCompanyPhone(personalInfo.getCompanyPhone());
        personalInfoMongo.setOccupation(personalInfo.getOccupation());
        personalInfoMongo.setPosition(personalInfo.getPosition());
        personalInfoMongo.setAnnualIncome(personalInfo.getAnnualIncome());

        personalInfoRepository.save(personalInfoMongo);
        log.debug("保存个人信息数据成功");
    }

    /**
     * 保存信息概要数据
     */
    private void saveInfoSummary(InfoSummary infoSummary, String creditReportId) {
        if (infoSummary == null) {
            return;
        }

        InfoSummaryMongo infoSummaryMongo = new InfoSummaryMongo();
        infoSummaryMongo.setCreditReportId(creditReportId);
        infoSummaryMongo.setCreditAccountCount(infoSummary.getCreditAccountCount());
        infoSummaryMongo.setCreditCardCount(infoSummary.getCreditCardCount());
        infoSummaryMongo.setQuasiCreditCardCount(infoSummary.getQuasiCreditCardCount());
        infoSummaryMongo.setLoanAccountCount(infoSummary.getLoanAccountCount());
        infoSummaryMongo.setTotalCreditLimit(infoSummary.getTotalCreditLimit());
        infoSummaryMongo.setUsedCreditLimit(infoSummary.getUsedCreditLimit());
        infoSummaryMongo.setAvgRepaymentLast6Months(infoSummary.getAvgRepaymentLast6Months());
        infoSummaryMongo.setOverdueAccountCount(infoSummary.getOverdueAccountCount());
        infoSummaryMongo.setOverdueAmount(infoSummary.getOverdueAmount());
        infoSummaryMongo.setBadDebtAccountCount(infoSummary.getBadDebtAccountCount());
        infoSummaryMongo.setBadDebtAmount(infoSummary.getBadDebtAmount());
        infoSummaryMongo.setLoanOverdueCountLast5Years(infoSummary.getLoanOverdueCountLast5Years());
        infoSummaryMongo.setCreditCardOverdueCountLast2Years(infoSummary.getCreditCardOverdueCountLast2Years());

        infoSummaryRepository.save(infoSummaryMongo);
        log.debug("保存信息概要数据成功");
    }

    /**
     * 保存信贷交易数据
     */
    private void saveCreditTransactions(List<CreditTransaction> creditTransactions, String creditReportId) {
        if (creditTransactions == null || creditTransactions.isEmpty()) {
            return;
        }

        for (CreditTransaction creditTransaction : creditTransactions) {
            CreditTransactionMongo creditTransactionMongo = new CreditTransactionMongo();
            creditTransactionMongo.setCreditReportId(creditReportId);
            creditTransactionMongo.setManagementOrg(creditTransaction.getManagementOrg());
            creditTransactionMongo.setAccountId(creditTransaction.getAccountId());
            creditTransactionMongo.setAccountType(creditTransaction.getAccountType());
            creditTransactionMongo.setCurrency(creditTransaction.getCurrency());
            creditTransactionMongo.setOpenDate(creditTransaction.getOpenDate());
            creditTransactionMongo.setExpireDate(creditTransaction.getExpireDate());
            creditTransactionMongo.setCreditLimit(creditTransaction.getCreditLimit());
            creditTransactionMongo.setSharedCreditLimit(creditTransaction.getSharedCreditLimit());
            creditTransactionMongo.setAccountStatus(creditTransaction.getAccountStatus());
            creditTransactionMongo.setRepaymentFrequency(creditTransaction.getRepaymentFrequency());
            creditTransactionMongo.setRepaymentPeriods(creditTransaction.getRepaymentPeriods());
            creditTransactionMongo.setCurrentRepayment(creditTransaction.getCurrentRepayment());

            creditTransactionRepository.save(creditTransactionMongo);
        }
        log.debug("保存信贷交易数据成功，共{}条记录", creditTransactions.size());
    }

    /**
     * 保存查询记录数据
     */
    private void saveQueryRecords(List<QueryRecord> queryRecords, String creditReportId) {
        if (queryRecords == null || queryRecords.isEmpty()) {
            return;
        }

        for (QueryRecord queryRecord : queryRecords) {
            QueryRecordMongo queryRecordMongo = new QueryRecordMongo();
            queryRecordMongo.setCreditReportId(creditReportId);
            queryRecordMongo.setQueryDate(queryRecord.getQueryDate());
            queryRecordMongo.setQueryOrg(queryRecord.getQueryOrg());
            queryRecordMongo.setQueryReason(queryRecord.getQueryReason());
            queryRecordMongo.setQueryType(queryRecord.getQueryType());

            queryRecordRepository.save(queryRecordMongo);
        }
        log.debug("保存查询记录数据成功，共{}条记录", queryRecords.size());
    }

    /**
     * 保存公共信息数据
     */
    private void savePublicInfos(List<PublicInfo> publicInfos, String creditReportId) {
        if (publicInfos == null || publicInfos.isEmpty()) {
            return;
        }

        for (PublicInfo publicInfo : publicInfos) {
            PublicInfoMongo publicInfoMongo = new PublicInfoMongo();
            publicInfoMongo.setCreditReportId(creditReportId);
            publicInfoMongo.setInfoType(publicInfo.getInfoType());
            publicInfoMongo.setRecordOrg(publicInfo.getRecordOrg());
            publicInfoMongo.setRecordDate(publicInfo.getRecordDate());
            publicInfoMongo.setPublishDate(publicInfo.getPublishDate());
            publicInfoMongo.setClosureType(publicInfo.getClosureType());
            publicInfoMongo.setClosureDate(publicInfo.getClosureDate());
            publicInfoMongo.setAmount(publicInfo.getAmount());
            publicInfoMongo.setDescription(publicInfo.getDescription());

            publicInfoRepository.save(publicInfoMongo);
        }
        log.debug("保存公共信息数据成功，共{}条记录", publicInfos.size());
    }

    /**
     * 保存非信贷交易数据
     */
    private void saveNonCreditTransactions(List<NonCreditTransaction> nonCreditTransactions, String creditReportId) {
        if (nonCreditTransactions == null || nonCreditTransactions.isEmpty()) {
            return;
        }

        for (NonCreditTransaction nonCreditTransaction : nonCreditTransactions) {
            NonCreditTransactionMongo nonCreditTransactionMongo = new NonCreditTransactionMongo();
            nonCreditTransactionMongo.setCreditReportId(creditReportId);
            nonCreditTransactionMongo.setBusinessType(nonCreditTransaction.getBusinessType());
            nonCreditTransactionMongo.setBusinessOrg(nonCreditTransaction.getBusinessOrg());
            nonCreditTransactionMongo.setOpenDate(nonCreditTransaction.getOpenDate());
            nonCreditTransactionMongo.setAccountStatus(nonCreditTransaction.getAccountStatus());
            nonCreditTransactionMongo.setPaymentHistory24Months(nonCreditTransaction.getPaymentHistory24Months());
            nonCreditTransactionMongo.setOverdueAmount(nonCreditTransaction.getOverdueAmount());
            nonCreditTransactionMongo.setOverdueMonths(nonCreditTransaction.getOverdueMonths());

            nonCreditTransactionRepository.save(nonCreditTransactionMongo);
        }
        log.debug("保存非信贷交易数据成功，共{}条记录", nonCreditTransactions.size());
    }

    /**
     * 脱敏身份证号码
     */
    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 6) {
            return "****";
        }
        return idCard.substring(0, 3) + "****" + idCard.substring(idCard.length() - 4);
    }
}
