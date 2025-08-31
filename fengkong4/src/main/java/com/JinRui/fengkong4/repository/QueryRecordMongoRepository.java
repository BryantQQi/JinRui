package com.JinRui.fengkong4.repository;

import com.JinRui.fengkong4.entity.mongo.QueryRecordMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 查询记录Repository
 * 
 * @author JinRui
 */
@Repository
public interface QueryRecordMongoRepository extends MongoRepository<QueryRecordMongo, String> {

    /**
     * 根据征信报文ID查询查询记录
     * 
     * @param creditReportId 征信报文ID
     * @return 查询记录列表
     */
    List<QueryRecordMongo> findByCreditReportId(String creditReportId);

    /**
     * 根据征信报文ID删除查询记录
     * 
     * @param creditReportId 征信报文ID
     */
    void deleteByCreditReportId(String creditReportId);
}
