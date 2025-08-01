/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.demo.service;

import io.boot.commons.mybatis.service.BaseService;
import io.boot.demo.entity.UserEntity;
import io.boot.demo.dto.LoginDTO;

import java.util.Map;

/**
 * 用户
 * 
 * @author epiphyllum.zhou@gmail.com
 */
public interface UserService extends BaseService<UserEntity> {

	UserEntity getByMobile(String mobile);

	UserEntity getUserByUserId(Long userId);

	/**
	 * 用户登录
	 * @param dto    登录表单
	 * @return        返回登录信息
	 */
	Map<String, Object> login(LoginDTO dto);
}
