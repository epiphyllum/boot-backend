/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.demo.service;

import io.boot.commons.mybatis.service.BaseService;
import io.boot.demo.entity.TokenEntity;

/**
 * 用户Token
 * 
 * @author epiphyllum.zhou@gmail.com
 */
public interface TokenService extends BaseService<TokenEntity> {

	TokenEntity getByToken(String token);

	/**
	 * 生成token
	 * @param userId  用户ID
	 * @return        返回token信息
	 */
	TokenEntity createToken(Long userId);

	/**
	 * 设置token过期
	 * @param userId 用户ID
	 */
	void expireToken(Long userId);

}
