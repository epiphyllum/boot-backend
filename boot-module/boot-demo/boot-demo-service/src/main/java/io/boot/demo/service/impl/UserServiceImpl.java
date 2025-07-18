/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.demo.service.impl;

import io.boot.commons.mybatis.service.impl.BaseServiceImpl;
import io.boot.commons.tools.exception.ErrorCode;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.validator.AssertUtils;
import io.boot.demo.dao.UserDao;
import io.boot.demo.entity.TokenEntity;
import io.boot.demo.entity.UserEntity;
import io.boot.demo.dto.LoginDTO;
import io.boot.demo.service.TokenService;
import io.boot.demo.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserDao, UserEntity> implements UserService {
    @Resource
    private TokenService tokenService;

    @Override
    public UserEntity getByMobile(String mobile) {
        return baseDao.getUserByMobile(mobile);
    }

    @Override
    public UserEntity getUserByUserId(Long userId) {
        return baseDao.getUserByUserId(userId);
    }

    @Override
    public Map<String, Object> login(LoginDTO dto) {
        UserEntity user = getByMobile(dto.getMobile());
        AssertUtils.isNull(user, ErrorCode.ACCOUNT_PASSWORD_ERROR);

        //密码错误
        if (!user.getPassword().equals(DigestUtils.sha256Hex(dto.getPassword()))) {
            throw new BootException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }

        //获取登录token
        TokenEntity tokenEntity = tokenService.createToken(user.getId());

        Map<String, Object> map = new HashMap<>(2);
        map.put("token", tokenEntity.getToken());
        map.put("expire", tokenEntity.getExpireDate().getTime() - System.currentTimeMillis());

        return map;
    }

}