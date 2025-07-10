/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.rbac.security.service;

import io.boot.commons.security.enums.UserStatusEnum;
import io.boot.commons.security.user.UserDetail;
import io.boot.commons.tools.exception.ErrorCode;
import io.boot.commons.tools.exception.BootException;
import io.boot.rbac.service.SysUserDetailService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class RenUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private SysUserDetailService sysUserDetailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetail userDetail = sysUserDetailService.getUserDetailByUsername(username);
        if (userDetail == null) {
            throw new BootException(ErrorCode.ACCOUNT_NOT_EXIST);
        }

        // 账号不可用
        if (userDetail.getStatus() == UserStatusEnum.DISABLE.value()) {
            userDetail.setEnabled(false);
        }

        return userDetail;
    }
}
