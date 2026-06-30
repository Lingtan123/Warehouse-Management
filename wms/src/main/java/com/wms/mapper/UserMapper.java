package com.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author nobody
 * @since 2026-05-26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> listAll();
    //使用自定义SQL，要么参数是ew，要么需要加注解，实际上经常使用的是加注解的方式
    //IPage<User> pageCC(Page<User> page, Wrapper<User> ew);
    IPage<User> pageCC(Page<User> page, @Param(Constants.WRAPPER) Wrapper<User>  wrapper);
}
