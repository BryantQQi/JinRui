package com.JinRui.fengkong4;

import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.service.CreditReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 征信报文服务测试类
 * 
 * @author JinRui
 */
@SpringBootTest
public class CreditReportServiceTest {

    @Autowired
    private CreditReportService creditReportService;

    @Test
    public void testGenerateRandomReport() {
        CreditReport report = creditReportService.generateRandomReport();
        
        assertNotNull(report);
        assertNotNull(report.getHeader());
        assertNotNull(report.getPersonalInfo());
        assertNotNull(report.getInfoSummary());
        
        // 验证报文
        assertTrue(creditReportService.validateReport(report));
        
        System.out.println("生成的报文编号：" + report.getHeader().getReportNumber());
        System.out.println("个人姓名：" + report.getPersonalInfo().getName());
        System.out.println("身份证号：" + report.getPersonalInfo().getIdNumber());
    }

    @Test
    public void testGenerateCustomReport() {
        CreditReport report = creditReportService.generateCustomReport(3, 5);
        
        assertNotNull(report);
        assertEquals(3, report.getCreditTransactions().size());
        assertEquals(5, report.getQueryRecords().size());
        
        // 验证报文
        assertTrue(creditReportService.validateReport(report));
    }

    @Test
    public void testConvertToXml() {
        CreditReport report = creditReportService.generateRandomReport();
        String xml = creditReportService.convertToXml(report);
        
        assertNotNull(xml);
        assertTrue(xml.contains("<?xml"));
        assertTrue(xml.contains("CreditReport"));
        
        System.out.println("XML长度：" + xml.length());
    }

    @Test
    public void testConvertToJson() {
        CreditReport report = creditReportService.generateRandomReport();
        String json = creditReportService.convertToJson(report);
        
        assertNotNull(json);
        assertTrue(json.contains("header"));
        assertTrue(json.contains("personalInfo"));
        
        System.out.println("JSON长度：" + json.length());
    }
}
