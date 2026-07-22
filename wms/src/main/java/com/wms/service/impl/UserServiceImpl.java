package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.dto.UserQuery;
import com.wms.dto.UserRequest;
import com.wms.entity.User;
import com.wms.mapper.UserMapper;
import com.wms.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nobody
 * @since 2026-05-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public List<User> listAll(){
        return userMapper.listAll();
    }

    public IPage<User> pageCC(Page<User> page, Wrapper<User> wrapper){
        return userMapper.pageCC(page,wrapper);
    }

    @Override
    public boolean createUser(UserRequest request) {
        User user = new User();
        BeanUtils.copyProperties(request,user);
        boolean saved = save(user);
        if (saved) {
            request.setId(user.getId());
        }
        return saved;
    }

    @Override
    public boolean updateUser(UserRequest request) {
        User user = new User();
        BeanUtils.copyProperties(request,user);
        return updateById(user);
    }

    @Override
    public boolean saveOrUpdateUser(UserRequest request) {
        User user = new User();
        BeanUtils.copyProperties(request,user);
        boolean saved = saveOrUpdate(user);
        if (saved) {
            request.setId(user.getId());
        }
        return saved;
    }

    @Override
    public IPage<User> listPageC1(UserQuery query) {
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        String name = query.getName();
        String sex = query.getSex();
        String roleId = query.getRoleId();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(User::getName, name);
        }
        if (StringUtils.isNotBlank(sex)) {
            queryWrapper.like(User::getSex, sex);
        }
        if (StringUtils.isNotBlank(roleId)) {
            queryWrapper.like(User::getRoleId, roleId);
        }

        return this.page(page, queryWrapper);
    }

}

