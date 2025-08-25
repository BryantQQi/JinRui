package com.JinRui.fengkong4.service.impl;

import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.generator.CreditDataGenerator;
import com.JinRui.fengkong4.service.CreditReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 征信报文服务实现类
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class CreditReportServiceImpl implements CreditReportService {

    @Autowired
    private CreditDataGenerator dataGenerator;

    private final XmlMapper xmlMapper = new XmlMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CreditReport generateRandomReport() {
        log.info("开始生成随机征信报文");
        
        try {
            CreditReport report = dataGenerator.generateCreditReport();
            log.info("随机征信报文生成成功，报文编号：{}", report.getHeader().getReportNumber());
            return report;
        } catch (Exception e) {
            log.error("生成随机征信报文失败", e);
            throw new RuntimeException("生成征信报文失败", e);
        }
    }

    @Override
    public CreditReport generateCustomReport(int creditRecords, int queryRecords) {
        log.info("开始生成自定义征信报文，信贷记录数：{}，查询记录数：{}", creditRecords, queryRecords);
        
        try {
            CreditReport report = new CreditReport();
            
            // 生成基础信息
            report.setHeader(dataGenerator.generateReportHeader());
            report.setPersonalInfo(dataGenerator.generatePersonalInfo());
            report.setInfoSummary(dataGenerator.generateInfoSummary());
            report.setStatementInfo(dataGenerator.generateStatementInfo());
            
            // 生成自定义数量的记录
            report.setCreditTransactions(dataGenerator.generateCreditTransactions(creditRecords));
            report.setNonCreditTransactions(dataGenerator.generateNonCreditTransactions(3));
            report.setPublicInfos(dataGenerator.generatePublicInfos(1));
            report.setQueryRecords(dataGenerator.generateQueryRecords(queryRecords));
            
            log.info("自定义征信报文生成成功，报文编号：{}", report.getHeader().getReportNumber());
            return report;
        } catch (Exception e) {
            log.error("生成自定义征信报文失败", e);
            throw new RuntimeException("生成征信报文失败", e);
        }
    }

    @Override
    public String convertToXml(CreditReport report) {
        log.debug("开始将征信报文转换为XML格式");
        
        try {
            String xml = xmlMapper.writeValueAsString(report);
            log.debug("征信报文XML转换成功，长度：{}", xml.length());
            return xml;
        } catch (JsonProcessingException e) {
            log.error("征信报文XML转换失败", e);
            throw new RuntimeException("XML转换失败", e);
        }
    }

    @Override
    public String convertToJson(CreditReport report) {
        log.debug("开始将征信报文转换为JSON格式");
        
        try {
            String json = objectMapper.writeValueAsString(report);
            log.debug("征信报文JSON转换成功，长度：{}", json.length());
            return json;
        } catch (JsonProcessingException e) {
            log.error("征信报文JSON转换失败", e);
            throw new RuntimeException("JSON转换失败", e);
        }
    }

    @Override
    public boolean validateReport(CreditReport report) {
        log.debug("开始验证征信报文数据完整性");
        
        try {
            // 验证报文头
            if (report.getHeader() == null) {
                log.warn("征信报文头信息为空");
                return false;
            }
            
            if (StringUtils.isEmpty(report.getHeader().getReportNumber())) {
                log.warn("征信报文编号为空");
                return false;
            }
            
            if (StringUtils.isEmpty(report.getHeader().getVersion())) {
                log.warn("征信报文版本为空");
                return false;
            }
            
            // 验证个人信息
            if (report.getPersonalInfo() == null) {
                log.warn("个人基本信息为空");
                return false;
            }
            
            if (StringUtils.isEmpty(report.getPersonalInfo().getName())) {
                log.warn("个人姓名为空");
                return false;
            }
            
            if (StringUtils.isEmpty(report.getPersonalInfo().getIdNumber())) {
                log.warn("身份证号码为空");
                return false;
            }
            
            // 验证信息概要
            if (report.getInfoSummary() == null) {
                log.warn("信息概要为空");
                return false;
            }
            
            // 验证信贷交易信息
            if (report.getCreditTransactions() != null) {
                for (int i = 0; i < report.getCreditTransactions().size(); i++) {
                    if (StringUtils.isEmpty(report.getCreditTransactions().get(i).getAccountId())) {
                        log.warn("第{}条信贷交易记录账户标识为空", i + 1);
                        return false;
                    }
                }
            }
            
            log.debug("征信报文数据验证通过");
            return true;
            
        } catch (Exception e) {
            log.error("征信报文数据验证失败", e);
            return false;
        }
    }
}
