package com.wms.mapper;

import com.wms.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
}
