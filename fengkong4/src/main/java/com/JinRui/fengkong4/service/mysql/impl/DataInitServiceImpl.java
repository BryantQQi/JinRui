package com.JinRui.fengkong4.service.mysql.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.JinRui.fengkong4.entity.mongo.*;
import com.JinRui.fengkong4.entity.mysql.*;
import com.JinRui.fengkong4.mapper.*;
import com.JinRui.fengkong4.repository.*;
import com.JinRui.fengkong4.service.mysql.DataInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据初始化服务实现类
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class DataInitServiceImpl implements DataInitService {

    // MongoDB Repository
    @Autowired
    private CreditReportMongoRepository creditReportMongoRepository;
    @Autowired
    private PersonalInfoMongoRepository personalInfoMongoRepository;
    @Autowired
    private CreditTransactionMongoRepository creditTransactionMongoRepository;
    @Autowired
    private InfoSummaryMongoRepository infoSummaryMongoRepository;
    @Autowired
    private QueryRecordMongoRepository queryRecordMongoRepository;
    @Autowired
    private PublicInfoMongoRepository publicInfoMongoRepository;
    @Autowired
    private NonCreditTransactionMongoRepository nonCreditTransactionMongoRepository;

    // MySQL Mapper
    @Autowired
    private CreditReportMapper creditReportMapper;
    @Autowired
    private PersonalInfoMapper personalInfoMapper;
    @Autowired
    private CreditTransactionMapper creditTransactionMapper;
    @Autowired
    private InfoSummaryMapper infoSummaryMapper;
    @Autowired
    private QueryRecordMapper queryRecordMapper;
    @Autowired
    private PublicInfoMapper publicInfoMapper;
    @Autowired
    private NonCreditTransactionMapper nonCreditTransactionMapper;

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public String initAllData() {
        log.info("开始初始化所有数据从MongoDB到MySQL");
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 先清空MySQL数据
            clearAllMysqlData();
            
            // 初始化各表数据
            int creditReports = initCreditReports();
            int personalInfos = initPersonalInfos();
            int creditTransactions = initCreditTransactions();
            int infoSummaries = initInfoSummaries();
            int queryRecords = initQueryRecords();
            int publicInfos = initPublicInfos();
            int nonCreditTransactions = initNonCreditTransactions();
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            String result = String.format(
                "数据初始化完成！耗时: %d ms\n" +
                "征信报文主记录: %d 条\n" +
                "个人基本信息: %d 条\n" +
                "信贷交易信息: %d 条\n" +
                "信息概要: %d 条\n" +
                "查询记录: %d 条\n" +
                "公共信息: %d 条\n" +
                "非信贷交易信息: %d 条\n" +
                "总计: %d 条",
                duration,
                creditReports,
                personalInfos,
                creditTransactions,
                infoSummaries,
                queryRecords,
                publicInfos,
                nonCreditTransactions,
                creditReports + personalInfos + creditTransactions + infoSummaries + 
                queryRecords + publicInfos + nonCreditTransactions
            );
            
            log.info(result);
            return result;
            
        } catch (Exception e) {
            log.error("数据初始化失败", e);
            throw new RuntimeException("数据初始化失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public int initCreditReports() {
        log.info("开始初始化征信报文主记录");
        
        List<CreditReportMongo> mongoList = creditReportMongoRepository.findAll();
        AtomicInteger count = new AtomicInteger(0);
        
        mongoList.forEach(mongo -> {
            CreditReportMysql mysql = new CreditReportMysql();
            BeanUtils.copyProperties(mongo, mysql);
            creditReportMapper.insert(mysql);
            count.incrementAndGet();
        });
        
        log.info("征信报文主记录初始化完成，共 {} 条", count.get());
        return count.get();
    }

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public int initPersonalInfos() {
        log.info("开始初始化个人基本信息");
        
        List<PersonalInfoMongo> mongoList = personalInfoMongoRepository.findAll();
        AtomicInteger count = new AtomicInteger(0);
        
        mongoList.forEach(mongo -> {
            PersonalInfoMysql mysql = new PersonalInfoMysql();
            BeanUtils.copyProperties(mongo, mysql);
            personalInfoMapper.insert(mysql);
            count.incrementAndGet();
        });
        
        log.info("个人基本信息初始化完成，共 {} 条", count.get());
        return count.get();
    }

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public int initCreditTransactions() {
        log.info("开始初始化信贷交易信息");
        
        List<CreditTransactionMongo> mongoList = creditTransactionMongoRepository.findAll();
        AtomicInteger count = new AtomicInteger(0);
        
        mongoList.forEach(mongo -> {
            CreditTransactionMysql mysql = new CreditTransactionMysql();
            BeanUtils.copyProperties(mongo, mysql);
            creditTransactionMapper.insert(mysql);
            count.incrementAndGet();
        });
        
        log.info("信贷交易信息初始化完成，共 {} 条", count.get());
        return count.get();
    }

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public int initInfoSummaries() {
        log.info("开始初始化信息概要");
        
        List<InfoSummaryMongo> mongoList = infoSummaryMongoRepository.findAll();
        AtomicInteger count = new AtomicInteger(0);
        
        mongoList.forEach(mongo -> {
            InfoSummaryMysql mysql = new InfoSummaryMysql();
            BeanUtils.copyProperties(mongo, mysql);
            infoSummaryMapper.insert(mysql);
            count.incrementAndGet();
        });
        
        log.info("信息概要初始化完成，共 {} 条", count.get());
        return count.get();
    }

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public int initQueryRecords() {
        log.info("开始初始化查询记录");
        
        List<QueryRecordMongo> mongoList = queryRecordMongoRepository.findAll();
        AtomicInteger count = new AtomicInteger(0);
        
        mongoList.forEach(mongo -> {
            QueryRecordMysql mysql = new QueryRecordMysql();
            BeanUtils.copyProperties(mongo, mysql);
            queryRecordMapper.insert(mysql);
            count.incrementAndGet();
        });
        
        log.info("查询记录初始化完成，共 {} 条", count.get());
        return count.get();
    }

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public int initPublicInfos() {
        log.info("开始初始化公共信息");
        
        List<PublicInfoMongo> mongoList = publicInfoMongoRepository.findAll();
        AtomicInteger count = new AtomicInteger(0);
        
        mongoList.forEach(mongo -> {
            PublicInfoMysql mysql = new PublicInfoMysql();
            BeanUtils.copyProperties(mongo, mysql);
            publicInfoMapper.insert(mysql);
            count.incrementAndGet();
        });
        
        log.info("公共信息初始化完成，共 {} 条", count.get());
        return count.get();
    }

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public int initNonCreditTransactions() {
        log.info("开始初始化非信贷交易信息");
        
        List<NonCreditTransactionMongo> mongoList = nonCreditTransactionMongoRepository.findAll();
        AtomicInteger count = new AtomicInteger(0);
        
        mongoList.forEach(mongo -> {
            NonCreditTransactionMysql mysql = new NonCreditTransactionMysql();
            BeanUtils.copyProperties(mongo, mysql);
            nonCreditTransactionMapper.insert(mysql);
            count.incrementAndGet();
        });
        
        log.info("非信贷交易信息初始化完成，共 {} 条", count.get());
        return count.get();
    }

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public String clearAllMysqlData() {
        log.info("开始清空MySQL所有表数据");
        
        try {
            // 清空所有表数据（按照外键依赖关系的逆序）
            nonCreditTransactionMapper.delete(new QueryWrapper<>());
            publicInfoMapper.delete(new QueryWrapper<>());
            queryRecordMapper.delete(new QueryWrapper<>());
            infoSummaryMapper.delete(new QueryWrapper<>());
            creditTransactionMapper.delete(new QueryWrapper<>());
            personalInfoMapper.delete(new QueryWrapper<>());
            creditReportMapper.delete(new QueryWrapper<>());
            
            log.info("MySQL所有表数据清空完成");
            return "MySQL所有表数据清空完成";
            
        } catch (Exception e) {
            log.error("清空MySQL数据失败", e);
            throw new RuntimeException("清空MySQL数据失败: " + e.getMessage());
        }
    }
}
