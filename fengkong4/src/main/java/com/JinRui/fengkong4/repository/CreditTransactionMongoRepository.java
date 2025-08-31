package com.JinRui.fengkong4.repository;

import com.JinRui.fengkong4.entity.mongo.CreditTransactionMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 信贷交易信息明细Repository
 * 
 * @author JinRui
 */
@Repository
public interface CreditTransactionMongoRepository extends MongoRepository<CreditTransactionMongo, String> {

    /**
     * 根据征信报文ID查询信贷交易记录
     * 
     * @param creditReportId 征信报文ID
     * @return 信贷交易记录列表
     */
    List<CreditTransactionMongo> findByCreditReportId(String creditReportId);

    /**
     * 根据征信报文ID删除信贷交易记录
     * 
     * @param creditReportId 征信报文ID
     */
    void deleteByCreditReportId(String creditReportId);
}
