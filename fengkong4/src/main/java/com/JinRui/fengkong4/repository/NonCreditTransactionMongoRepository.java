package com.JinRui.fengkong4.repository;

import com.JinRui.fengkong4.entity.mongo.NonCreditTransactionMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 非信贷交易信息明细Repository
 * 
 * @author JinRui
 */
@Repository
public interface NonCreditTransactionMongoRepository extends MongoRepository<NonCreditTransactionMongo, String> {

    /**
     * 根据征信报文ID查询非信贷交易记录
     * 
     * @param creditReportId 征信报文ID
     * @return 非信贷交易记录列表
     */
    List<NonCreditTransactionMongo> findByCreditReportId(String creditReportId);

    /**
     * 根据征信报文ID删除非信贷交易记录
     * 
     * @param creditReportId 征信报文ID
     */
    void deleteByCreditReportId(String creditReportId);
}
