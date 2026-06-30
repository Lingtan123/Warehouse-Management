package com.wms.service;

import com.wms.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nobody
 * @since 2026-05-26
 */
public interface IUserService extends IService<User> {
    List<User> listAll();


}
