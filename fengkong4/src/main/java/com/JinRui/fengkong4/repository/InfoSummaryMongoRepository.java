package com.JinRui.fengkong4.repository;

import com.JinRui.fengkong4.entity.mongo.InfoSummaryMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 信息概要Repository
 * 
 * @author JinRui
 */
@Repository
public interface InfoSummaryMongoRepository extends MongoRepository<InfoSummaryMongo, String> {

    /**
     * 根据征信报文ID查询信息概要
     * 
     * @param creditReportId 征信报文ID
     * @return 信息概要列表
     */
    List<InfoSummaryMongo> findByCreditReportId(String creditReportId);

    /**
     * 根据征信报文ID删除信息概要
     * 
     * @param creditReportId 征信报文ID
     */
    void deleteByCreditReportId(String creditReportId);
}
