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
 * @since 2026-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单编码
     */
    @TableField("menuCode")
    private String menuCode;

    /**
     * 菜单名字
     */
    @TableField("menuName")
    private String menuName;

    /**
     * 菜单级别
     */
    @TableField("menuLevel")
    private String menuLevel;

    /**
     * 菜单的父Code
     */
    @TableField("menuParentCode")
    private String menuParentCode;

    /**
     * 点击触发的函数
     */
    @TableField("menuClick")
    private String menuClick;

    /**
     * 权限 0超级管理员 1项目组长 2员工 可以逗号组合使用
     */
    @TableField("menuRight")
    private String menuRight;

    /**
     * 跳转的页面文件
     */
    @TableField("menuComponent")
    private String menuComponent;

    @TableField("menuIcon")
    private String menuIcon;


}
