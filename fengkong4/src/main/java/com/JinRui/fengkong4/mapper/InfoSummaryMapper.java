package com.JinRui.fengkong4.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.JinRui.fengkong4.entity.mysql.InfoSummaryMysql;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 信息概要Mapper接口
 * 
 * @author JinRui
 */
@Mapper
public interface InfoSummaryMapper extends BaseMapper<InfoSummaryMysql> {

    /**
     * 根据征信报文ID查询信息概要
     * 
     * @param creditReportId 征信报文ID
     * @return 信息概要列表
     */
    List<InfoSummaryMysql> selectByCreditReportId(@Param("creditReportId") String creditReportId);

    /**
     * 根据征信报文ID删除信息概要
     * 
     * @param creditReportId 征信报文ID
     * @return 删除记录数
     */
    int deleteByCreditReportId(@Param("creditReportId") String creditReportId);
}
