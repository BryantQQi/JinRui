package com.JinRui.fengkong4.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.JinRui.fengkong4.entity.mysql.NonCreditTransactionMysql;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 非信贷交易信息明细Mapper接口
 * 
 * @author JinRui
 */
@Mapper
public interface NonCreditTransactionMapper extends BaseMapper<NonCreditTransactionMysql> {

    /**
     * 根据征信报文ID查询非信贷交易信息
     * 
     * @param creditReportId 征信报文ID
     * @return 非信贷交易信息列表
     */
    List<NonCreditTransactionMysql> selectByCreditReportId(@Param("creditReportId") String creditReportId);

    /**
     * 根据征信报文ID删除非信贷交易信息
     * 
     * @param creditReportId 征信报文ID
     * @return 删除记录数
     */
    int deleteByCreditReportId(@Param("creditReportId") String creditReportId);
}
