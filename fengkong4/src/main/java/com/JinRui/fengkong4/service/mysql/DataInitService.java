package com.JinRui.fengkong4.service.mysql;

/**
 * 数据初始化服务接口
 * 将MongoDB数据同步到MySQL
 * 
 * @author JinRui
 */
public interface DataInitService {

    /**
     * 初始化所有数据从MongoDB到MySQL
     * 
     * @return 初始化结果消息
     */
    String initAllData();

    /**
     * 初始化征信报文主记录
     * 
     * @return 同步记录数
     */
    int initCreditReports();

    /**
     * 初始化个人基本信息
     * 
     * @return 同步记录数
     */
    int initPersonalInfos();

    /**
     * 初始化信贷交易信息
     * 
     * @return 同步记录数
     */
    int initCreditTransactions();

    /**
     * 初始化信息概要
     * 
     * @return 同步记录数
     */
    int initInfoSummaries();

    /**
     * 初始化查询记录
     * 
     * @return 同步记录数
     */
    int initQueryRecords();

    /**
     * 初始化公共信息
     * 
     * @return 同步记录数
     */
    int initPublicInfos();

    /**
     * 初始化非信贷交易信息
     * 
     * @return 同步记录数
     */
    int initNonCreditTransactions();

    /**
     * 清空MySQL所有表数据
     * 
     * @return 清空结果消息
     */
    String clearAllMysqlData();
}
