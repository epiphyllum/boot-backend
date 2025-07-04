/**
 * Copyright (c) 2021 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.dto.AlipayNotifyLogDTO;
import io.boot.entity.AlipayNotifyLogEntity;

/**
 * 支付宝回调日志
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface AlipayNotifyLogService extends CrudService<AlipayNotifyLogEntity, AlipayNotifyLogDTO> {

}