package com.JinRui.fengkong4.repository;

import com.JinRui.fengkong4.entity.mongo.PersonalInfoMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 个人基本信息Repository
 * 
 * @author JinRui
 */
@Repository
public interface PersonalInfoMongoRepository extends MongoRepository<PersonalInfoMongo, String> {

    /**
     * 根据征信报文ID查询个人信息
     * 
     * @param creditReportId 征信报文ID
     * @return 个人信息列表
     */
    List<PersonalInfoMongo> findByCreditReportId(String creditReportId);

    /**
     * 根据征信报文ID删除个人信息
     * 
     * @param creditReportId 征信报文ID
     */
    void deleteByCreditReportId(String creditReportId);
}
