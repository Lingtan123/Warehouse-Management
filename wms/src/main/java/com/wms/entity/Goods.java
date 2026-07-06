package com.wms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author nobody
 * @since 2026-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 货物名称
     */
    @TableField("name")
    private String name;

    /**
     * 所处仓库
     */
    @TableField("storage")
    private Integer storage;

    /**
     * 所属分类
     */
    @TableField("goodstype")
    private Integer goodstype;

    /**
     * 数量
     */
    @TableField("count")
    private Integer count;

    /**
     * 备注
     */
    @TableField("remake")
    private String remake;


}
