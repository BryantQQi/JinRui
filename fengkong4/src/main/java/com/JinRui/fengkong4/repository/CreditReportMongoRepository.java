package com.JinRui.fengkong4.repository;

import com.JinRui.fengkong4.entity.mongo.CreditReportMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 征信报文主记录Repository
 * 
 * @author JinRui
 */
@Repository
public interface CreditReportMongoRepository extends MongoRepository<CreditReportMongo, String> {

    /**
     * 根据身份证号码和姓名查询征信记录
     * 
     * @param idNumber 身份证号码
     * @param name 姓名
     * @return 征信记录列表
     */
    List<CreditReportMongo> findByIdNumberAndName(String idNumber, String name);

    /**
     * 根据征信报文ID查询
     * 
     * @param creditReportId 征信报文ID
     * @return 征信记录
     */
    Optional<CreditReportMongo> findByCreditReportId(String creditReportId);

    /**
     * 根据身份证号码查询
     * 
     * @param idNumber 身份证号码
     * @return 征信记录列表
     */
    List<CreditReportMongo> findByIdNumber(String idNumber);

    /**
     * 检查是否已存在相同用户的征信数据
     * 
     * @param idNumber 身份证号码
     * @param name 姓名
     * @return 是否存在
     */
    boolean existsByIdNumberAndName(String idNumber, String name);
}
