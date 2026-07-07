package com.wms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @since 2026-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("record")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 货物id
     */
    @TableField("goods")
    private Integer goods;

    /**
     * 取货人/补货人
     */
    @TableField("userId")
    private Integer userId;

    /**
     * 操作人id
     */
    @TableField("adminId")
    private Integer adminId;

    /**
     * 数量
     */
    @TableField("count")
    private Integer count;

    /**
     * 操作时间
     */
    @TableField("time")
    private LocalDateTime time;

    /**
     * 备注
     */
    @TableField("remake")
    private String remake;

}
