/**
 * Copyright (c) 2021 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.dao;


import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.WeChatNotifyLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信支付回调日志
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface WeChatNotifyLogDao extends BaseDao<WeChatNotifyLogEntity> {

}