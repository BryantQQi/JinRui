package com.JinRui.fengkong4.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.JinRui.fengkong4.entity.mysql.PersonalInfoMysql;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 个人基本信息Mapper接口
 * 
 * @author JinRui
 */
@Mapper
public interface PersonalInfoMapper extends BaseMapper<PersonalInfoMysql> {

    /**
     * 根据征信报文ID查询个人信息
     * 
     * @param creditReportId 征信报文ID
     * @return 个人信息列表
     */
    List<PersonalInfoMysql> selectByCreditReportId(@Param("creditReportId") String creditReportId);

    /**
     * 根据征信报文ID删除个人信息
     * 
     * @param creditReportId 征信报文ID
     * @return 删除记录数
     */
    int deleteByCreditReportId(@Param("creditReportId") String creditReportId);
}
