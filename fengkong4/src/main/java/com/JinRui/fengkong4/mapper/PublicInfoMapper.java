package com.JinRui.fengkong4.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.JinRui.fengkong4.entity.mysql.PublicInfoMysql;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公共信息明细Mapper接口
 * 
 * @author JinRui
 */
@Mapper
public interface PublicInfoMapper extends BaseMapper<PublicInfoMysql> {

    /**
     * 根据征信报文ID查询公共信息
     * 
     * @param creditReportId 征信报文ID
     * @return 公共信息列表
     */
    List<PublicInfoMysql> selectByCreditReportId(@Param("creditReportId") String creditReportId);

    /**
     * 根据征信报文ID删除公共信息
     * 
     * @param creditReportId 征信报文ID
     * @return 删除记录数
     */
    int deleteByCreditReportId(@Param("creditReportId") String creditReportId);
}
