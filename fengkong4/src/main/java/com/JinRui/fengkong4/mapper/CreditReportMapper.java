package com.JinRui.fengkong4.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.JinRui.fengkong4.entity.mysql.CreditReportMysql;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 征信报文主记录Mapper接口
 * 
 * @author JinRui
 */
@Mapper
public interface CreditReportMapper extends BaseMapper<CreditReportMysql> {

    /**
     * 根据身份证号码和姓名查询征信记录
     * 
     * @param idNumber 身份证号码
     * @param name 姓名
     * @return 征信记录列表
     */
    List<CreditReportMysql> selectByIdNumberAndName(@Param("idNumber") String idNumber, @Param("name") String name);

    /**
     * 根据征信报文ID查询
     * 
     * @param creditReportId 征信报文ID
     * @return 征信记录
     */
    CreditReportMysql selectByCreditReportId(@Param("creditReportId") String creditReportId);

    /**
     * 根据身份证号码查询
     * 
     * @param idNumber 身份证号码
     * @return 征信记录列表
     */
    List<CreditReportMysql> selectByIdNumber(@Param("idNumber") String idNumber);

    /**
     * 检查是否已存在相同用户的征信数据
     * 
     * @param idNumber 身份证号码
     * @param name 姓名
     * @return 记录数量
     */
    int countByIdNumberAndName(@Param("idNumber") String idNumber, @Param("name") String name);
}
