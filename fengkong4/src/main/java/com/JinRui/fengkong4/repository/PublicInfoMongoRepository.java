package com.JinRui.fengkong4.repository;

import com.JinRui.fengkong4.entity.mongo.PublicInfoMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 公共信息明细Repository
 * 
 * @author JinRui
 */
@Repository
public interface PublicInfoMongoRepository extends MongoRepository<PublicInfoMongo, String> {

    /**
     * 根据征信报文ID查询公共信息
     * 
     * @param creditReportId 征信报文ID
     * @return 公共信息列表
     */
    List<PublicInfoMongo> findByCreditReportId(String creditReportId);

    /**
     * 根据征信报文ID删除公共信息
     * 
     * @param creditReportId 征信报文ID
     */
    void deleteByCreditReportId(String creditReportId);
}
