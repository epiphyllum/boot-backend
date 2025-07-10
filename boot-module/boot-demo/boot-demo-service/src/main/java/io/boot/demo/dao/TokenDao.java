/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.demo.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.demo.entity.TokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Token
 * 
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface TokenDao extends BaseDao<TokenEntity> {
    TokenEntity getByToken(String token);

    TokenEntity getByUserId(Long userId);
}
