package com.JinRui.task3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * ClassName:Ware
 * Package: com.JinRui.task3.entity
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/10 22:14
 * @Version 1.0
 */
@TableName("ware")
@Data
public class Ware {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("city")
    private String city;

    @TableField("district")
    private String district;

    @TableField("detail_address")
    private String detailAddress;

}
