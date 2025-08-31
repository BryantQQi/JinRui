package com.JinRui.fengkong4.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.JinRui.fengkong4.entity.mysql.QueryRecordMysql;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 查询记录Mapper接口
 * 
 * @author JinRui
 */
@Mapper
public interface QueryRecordMapper extends BaseMapper<QueryRecordMysql> {

    /**
     * 根据征信报文ID查询查询记录
     * 
     * @param creditReportId 征信报文ID
     * @return 查询记录列表
     */
    List<QueryRecordMysql> selectByCreditReportId(@Param("creditReportId") String creditReportId);

    /**
     * 根据征信报文ID删除查询记录
     * 
     * @param creditReportId 征信报文ID
     * @return 删除记录数
     */
    int deleteByCreditReportId(@Param("creditReportId") String creditReportId);
}
