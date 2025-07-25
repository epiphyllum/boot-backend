/**
 * Copyright (c) 2021 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.payment.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.payment.entity.AlipayNotifyLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 支付宝回调日志
*
* @author epiphyllum.zhou@gmail.com
*/
@Mapper
public interface AlipayNotifyLogDao extends BaseDao<AlipayNotifyLogEntity> {
	
}