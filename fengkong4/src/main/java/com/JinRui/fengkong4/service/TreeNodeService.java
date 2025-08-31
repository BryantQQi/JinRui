package com.JinRui.fengkong4.service;

import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.entity.TreeNode;
import com.JinRui.fengkong4.parser.ParseException;

/**
 * TreeNode转换和操作服务接口
 * 
 * @author JinRui
 */
public interface TreeNodeService {
    
    /**
     * 将CreditReport对象转换为TreeNode
     * 
     * @param report 征信报文对象
     * @return TreeNode 多叉树结构
     */
    TreeNode convertCreditReportToTree(CreditReport report);
    
    /**
     * 通过身份证和姓名调用征信接口并解析为TreeNode
     * 
     * @param idCard 身份证号码
     * @param name 姓名
     * @return TreeNode 解析后的多叉树结构
     * @throws ParseException 解析异常
     */
    TreeNode callAndParseReport(String idCard, String name) throws ParseException;
    
    /**
     * 获取随机的API端点
     * 
     * @return API端点路径
     */
    String getRandomApiEndpoint();
    
    /**
     * 验证身份证号码格式
     * 
     * @param idCard 身份证号码
     * @return 是否有效
     */
    boolean validateIdCard(String idCard);
    
    /**
     * 验证姓名格式
     * 
     * @param name 姓名
     * @return 是否有效
     */
    boolean validateName(String name);
    
    /**
     * 调用指定的征信接口获取报文数据
     * 
     * @param endpoint 接口端点
     * @param idCard 身份证号码
     * @param name 姓名
     * @return 报文数据和格式信息
     * @throws Exception 调用异常
     */
    ReportDataResult callCreditReportApi(String endpoint, String idCard, String name) throws Exception;
    
    /**
     * 报文数据结果封装类
     */
    class ReportDataResult {
        private String data;
        private String format;
        
        public ReportDataResult(String data, String format) {
            this.data = data;
            this.format = format;
        }
        
        public String getData() {
            return data;
        }
        
        public void setData(String data) {
            this.data = data;
        }
        
        public String getFormat() {
            return format;
        }
        
        public void setFormat(String format) {
            this.format = format;
        }
    }
}
