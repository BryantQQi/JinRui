package com.target.target1.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * ClassName:SkuInfo
 * Package: com.target.target1.entity
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/7 15:40
 * @Version 1.0
 */
@Data
@TableName("sku_info")
public class SkuInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("category_id")
    private Long categoryId;

    @TableField("stock")
    private Integer stock;

}
