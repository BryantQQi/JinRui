package com.JinRui.fengkong4.service.impl;

import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.factory.ReportParserFactory;
import com.JinRui.fengkong4.parser.ParseException;
import com.JinRui.fengkong4.service.CreditReportParseService;
import com.JinRui.fengkong4.service.TreeNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 征信报文解析服务实现类
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class CreditReportParseServiceImpl implements CreditReportParseService {

    @Autowired
    private ReportParserFactory reportParserFactory;

    @Autowired
    private TreeNodeService treeNodeService;

    @Override
    public CreditReport getCreditReportByIdAndName(String idCard, String name) throws ParseException {
        log.info("开始获取并解析征信报文，身份证：{}，姓名：{}", maskIdCard(idCard), name);

        try {
            // 参数验证
            if (!treeNodeService.validateIdCard(idCard)) {
                throw new ParseException("身份证号码格式不正确", "INVALID_ID_CARD", "VALIDATION");
            }

            if (!treeNodeService.validateName(name)) {
                throw new ParseException("姓名格式不正确", "INVALID_NAME", "VALIDATION");
            }

            // 随机选择API端点
            String endpoint = treeNodeService.getRandomApiEndpoint();
            log.info("随机选择的API端点：{}", endpoint);

            // 调用征信接口获取原始报文数据
            TreeNodeService.ReportDataResult result = treeNodeService.callCreditReportApi(endpoint, idCard, name);

            // 直接解析为CreditReport对象
            CreditReport creditReport = reportParserFactory.parseToCreditReport(result.getData());

            log.info("征信报文获取和解析完成");
            return creditReport;

        } catch (ParseException e) {
            log.error("获取并解析征信报文失败", e);
            throw e;
        } catch (Exception e) {
            log.error("获取并解析征信报文失败", e);
            throw new ParseException("获取并解析征信报文失败：" + e.getMessage(), e, "PARSE_ERROR", "SERVICE");
        }
    }

    @Override
    public CreditReport parseCreditReport(String reportData) throws ParseException {
        log.info("开始解析征信报文数据，数据长度：{}", reportData != null ? reportData.length() : 0);

        try {
            CreditReport creditReport = reportParserFactory.parseToCreditReport(reportData);
            log.info("征信报文数据解析完成");
            return creditReport;

        } catch (ParseException e) {
            log.error("解析征信报文数据失败", e);
            throw e;
        } catch (Exception e) {
            log.error("解析征信报文数据失败", e);
            throw new ParseException("解析征信报文数据失败：" + e.getMessage(), e, "PARSE_ERROR", "SERVICE");
        }
    }

    @Override
    public CreditReport parseCreditReportByFormat(String reportData, String format) throws ParseException {
        log.info("开始按指定格式解析征信报文数据，格式：{}，数据长度：{}", 
                format, reportData != null ? reportData.length() : 0);

        try {
            CreditReport creditReport = reportParserFactory.parseToCreditReportByFormat(reportData, format);
            log.info("征信报文数据按指定格式解析完成");
            return creditReport;

        } catch (ParseException e) {
            log.error("按指定格式解析征信报文数据失败", e);
            throw e;
        } catch (Exception e) {
            log.error("按指定格式解析征信报文数据失败", e);
            throw new ParseException("按指定格式解析征信报文数据失败：" + e.getMessage(), e, "PARSE_ERROR", "SERVICE");
        }
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
