package com.JinRui.target2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * ClassName:User
 * Package: com.JinRui.target2.entity
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/8 17:26
 * @Version 1.0
 */
@TableName("user")
@Data
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("nick_name")
    private String nickName;

    @TableField("sex")
    private String sex;

    @TableField("phone")
    private String phone;
}
