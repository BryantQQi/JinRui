package com.JinRui.fengkong4.generator;

import com.JinRui.fengkong4.entity.*;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 征信数据随机生成器
 * 
 * @author JinRui
 */
@Component
public class CreditDataGenerator {

    private final Faker faker = new Faker(new Locale("zh-CN"));
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Random random = new Random();

    /**
     * 生成完整的征信报文
     */
    public CreditReport generateCreditReport() {
        CreditReport report = new CreditReport();
        
        report.setHeader(generateReportHeader());
        report.setPersonalInfo(generatePersonalInfo());
        report.setInfoSummary(generateInfoSummary());
        report.setCreditTransactions(generateCreditTransactions(5));
        report.setNonCreditTransactions(generateNonCreditTransactions(3));
        report.setPublicInfos(generatePublicInfos(2));
        report.setQueryRecords(generateQueryRecords(8));
        report.setStatementInfo(generateStatementInfo());
        
        return report;
    }

    /**
     * 生成报文头信息
     */
    public ReportHeader generateReportHeader() {
        ReportHeader header = new ReportHeader();
        header.setVersion("2.0");
        header.setReportNumber("CR" + System.currentTimeMillis());
        header.setGenerateTime(dateTimeFormat.format(new Date()));
        header.setInstitutionCode("FENGKONG001");
        header.setQueryReason("贷款审批");
        header.setReportType("1");
        return header;
    }

    /**
     * 生成个人基本信息
     */
    public PersonalInfo generatePersonalInfo() {
        PersonalInfo info = new PersonalInfo();
        
        info.setName(faker.name().fullName());
        info.setIdType("1"); // 身份证
        info.setIdNumber(generateIdNumber());
        info.setGender(random.nextBoolean() ? "1" : "2"); // 1-男, 2-女
        info.setBirthDate(generateBirthDate());
        info.setMaritalStatus(String.valueOf(random.nextInt(4) + 1)); // 1-4
        info.setEducation(String.valueOf(random.nextInt(9) + 1)); // 1-9
        info.setDegree(String.valueOf(random.nextInt(4) + 1)); // 1-4
        info.setEmploymentStatus("1"); // 就业
        info.setNationality("中国");
        info.setEmail(faker.internet().emailAddress());
        info.setAddress(faker.address().fullAddress());
        info.setMobile(generateMobile());
        info.setCompanyName(faker.company().name());
        info.setCompanyAddress(faker.address().fullAddress());
        info.setCompanyPhone(generatePhone());
        info.setOccupation(faker.job().title());
        info.setPosition(faker.job().position());
        info.setAnnualIncome(String.valueOf((random.nextInt(50) + 10) * 10000)); // 10-60万
        
        return info;
    }

    /**
     * 生成信息概要
     */
    public InfoSummary generateInfoSummary() {
        InfoSummary summary = new InfoSummary();
        
        summary.setCreditAccountCount(random.nextInt(10) + 1);
        summary.setCreditCardCount(random.nextInt(5) + 1);
        summary.setQuasiCreditCardCount(random.nextInt(2));
        summary.setLoanAccountCount(random.nextInt(3) + 1);
        summary.setTotalCreditLimit(Long.valueOf(random.nextInt(500000) + 50000));
        summary.setUsedCreditLimit(Long.valueOf(random.nextInt(200000) + 10000));
        summary.setAvgRepaymentLast6Months(Long.valueOf(random.nextInt(10000) + 1000));
        summary.setOverdueAccountCount(random.nextInt(2));
        summary.setOverdueAmount(Long.valueOf(random.nextInt(5000)));
        summary.setBadDebtAccountCount(0);
        summary.setBadDebtAmount(0L);
        summary.setLoanOverdueCountLast5Years(random.nextInt(3));
        summary.setCreditCardOverdueCountLast2Years(random.nextInt(2));
        
        return summary;
    }

    /**
     * 生成信贷交易信息
     */
    public List<CreditTransaction> generateCreditTransactions(int count) {
        List<CreditTransaction> transactions = new ArrayList<>();
        String[] banks = {"中国工商银行", "中国建设银行", "中国银行", "中国农业银行", "招商银行", "交通银行"};
        String[] accountTypes = {"1", "2", "3"}; // 贷记卡、准贷记卡、贷款
        
        for (int i = 0; i < count; i++) {
            CreditTransaction transaction = new CreditTransaction();
            
            transaction.setManagementOrg(banks[random.nextInt(banks.length)]);
            transaction.setAccountId("ACC" + System.currentTimeMillis() + i);
            transaction.setAccountType(accountTypes[random.nextInt(accountTypes.length)]);
            transaction.setCurrency("CNY");
            transaction.setOpenDate(generatePastDate(365 * 5)); // 5年内开户
            transaction.setExpireDate(generateFutureDate(365 * 2)); // 2年后到期
            transaction.setCreditLimit(Long.valueOf(random.nextInt(100000) + 10000));
            transaction.setSharedCreditLimit(Long.valueOf(random.nextInt(50000)));
            transaction.setAccountStatus("1"); // 正常
            transaction.setRepaymentFrequency("1"); // 月
            transaction.setRepaymentPeriods(random.nextInt(36) + 12);
            transaction.setCurrentRepayment(Long.valueOf(random.nextInt(5000) + 500));
            transaction.setBalance(Long.valueOf(random.nextInt(50000) + 1000));
            transaction.setRemainingPeriods(random.nextInt(24) + 1);
            transaction.setActualRepayment(Long.valueOf(random.nextInt(5000) + 500));
            transaction.setCurrentOverduePeriods(random.nextInt(2));
            transaction.setCurrentOverdueAmount(Long.valueOf(random.nextInt(1000)));
            transaction.setOverdue31To60Days(0L);
            transaction.setOverdue61To90Days(0L);
            transaction.setOverdue91To180Days(0L);
            transaction.setOverdueOver180Days(0L);
            transaction.setRepaymentHistory24Months(generateRepaymentHistory());
            
            transactions.add(transaction);
        }
        
        return transactions;
    }

    /**
     * 生成非信贷交易信息
     */
    public List<NonCreditTransaction> generateNonCreditTransactions(int count) {
        List<NonCreditTransaction> transactions = new ArrayList<>();
        String[] businessTypes = {"1", "2", "3", "4"}; // 电信、水费、电费、燃气费
        String[] businessOrgs = {"中国电信", "国家电网", "自来水公司", "燃气公司"};
        
        for (int i = 0; i < count; i++) {
            NonCreditTransaction transaction = new NonCreditTransaction();
            
            int typeIndex = random.nextInt(businessTypes.length);
            transaction.setBusinessType(businessTypes[typeIndex]);
            transaction.setBusinessOrg(businessOrgs[typeIndex]);
            transaction.setOpenDate(generatePastDate(365 * 3));
            transaction.setAccountStatus("1"); // 正常
            transaction.setPaymentHistory24Months(generatePaymentHistory());
            transaction.setOverdueAmount(Long.valueOf(random.nextInt(500)));
            transaction.setOverdueMonths(random.nextInt(2));
            
            transactions.add(transaction);
        }
        
        return transactions;
    }

    /**
     * 生成公共信息
     */
    public List<PublicInfo> generatePublicInfos(int count) {
        List<PublicInfo> infos = new ArrayList<>();
        String[] infoTypes = {"1", "2", "3", "4"}; // 欠税、民事判决、强制执行、行政处罚
        String[] recordOrgs = {"税务局", "人民法院", "执行局", "工商局"};
        
        for (int i = 0; i < count; i++) {
            if (random.nextDouble() > 0.3) continue; // 70%概率不生成公共信息
            
            PublicInfo info = new PublicInfo();
            
            int typeIndex = random.nextInt(infoTypes.length);
            info.setInfoType(infoTypes[typeIndex]);
            info.setRecordOrg(recordOrgs[typeIndex]);
            info.setRecordDate(generatePastDate(365 * 2));
            info.setPublishDate(generatePastDate(365));
            info.setClosureType("1"); // 已结案
            info.setClosureDate(generatePastDate(180));
            info.setAmount(Long.valueOf(random.nextInt(50000) + 1000));
            info.setDescription("相关处理记录");
            
            infos.add(info);
        }
        
        return infos;
    }

    /**
     * 生成查询记录
     */
    public List<QueryRecord> generateQueryRecords(int count) {
        List<QueryRecord> records = new ArrayList<>();
        String[] queryReasons = {"01", "02", "03", "04", "05"}; // 各种查询原因
        String[] queryOrgs = {"工商银行", "建设银行", "招商银行", "本人查询", "平安银行"};
        
        for (int i = 0; i < count; i++) {
            QueryRecord record = new QueryRecord();
            
            record.setQueryDate(generatePastDate(365));
            record.setQueryOrg(queryOrgs[random.nextInt(queryOrgs.length)]);
            record.setQueryReason(queryReasons[random.nextInt(queryReasons.length)]);
            record.setQueryType(record.getQueryOrg().equals("本人查询") ? "2" : "1");
            
            records.add(record);
        }
        
        return records;
    }

    /**
     * 生成标注及声明信息
     */
    public StatementInfo generateStatementInfo() {
        StatementInfo info = new StatementInfo();
        
        if (random.nextDouble() > 0.8) { // 20%概率有异议标注
            info.setDisputeNote("对某笔记录存在异议");
            info.setPersonalStatement("本人声明相关情况");
            info.setDisputeHandling("已处理完成");
        }
        
        return info;
    }

    // 辅助方法

    /**
     * 生成身份证号码
     */
    private String generateIdNumber() {
        StringBuilder sb = new StringBuilder();
        // 地区代码
        sb.append("110101");
        // 出生日期
        int year = 1970 + random.nextInt(40);
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        sb.append(String.format("%04d%02d%02d", year, month, day));
        // 顺序码
        sb.append(String.format("%03d", random.nextInt(1000)));
        // 校验码
        sb.append(random.nextInt(10));
        
        return sb.toString();
    }

    /**
     * 生成出生日期
     */
    private String generateBirthDate() {
        int year = 1970 + random.nextInt(40);
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * 生成手机号
     */
    private String generateMobile() {
        String[] prefixes = {"130", "131", "132", "133", "134", "135", "136", "137", "138", "139",
                            "150", "151", "152", "153", "155", "156", "157", "158", "159",
                            "180", "181", "182", "183", "184", "185", "186", "187", "188", "189"};
        String prefix = prefixes[random.nextInt(prefixes.length)];
        return prefix + String.format("%08d", random.nextInt(100000000));
    }

    /**
     * 生成固定电话
     */
    private String generatePhone() {
        return "010-" + String.format("%08d", random.nextInt(100000000));
    }

    /**
     * 生成过去的日期
     */
    private String generatePastDate(int maxDaysAgo) {
        LocalDate date = LocalDate.now().minusDays(random.nextInt(maxDaysAgo));
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 生成未来的日期
     */
    private String generateFutureDate(int maxDaysFromNow) {
        LocalDate date = LocalDate.now().plusDays(random.nextInt(maxDaysFromNow));
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 生成还款历史记录（24个月）
     */
    private String generateRepaymentHistory() {
        StringBuilder sb = new StringBuilder();
        String[] status = {"N", "1", "2", "3", "4", "5", "6", "7", "*", "/", "C", "G"};
        
        for (int i = 0; i < 24; i++) {
            if (random.nextDouble() > 0.1) { // 90%正常
                sb.append("N");
            } else {
                sb.append(status[random.nextInt(status.length)]);
            }
        }
        
        return sb.toString();
    }

    /**
     * 生成缴费历史记录（24个月）
     */
    private String generatePaymentHistory() {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < 24; i++) {
            if (random.nextDouble() > 0.05) { // 95%正常缴费
                sb.append("N");
            } else {
                sb.append("*"); // 欠费
            }
        }
        
        return sb.toString();
    }
}
