/**
 * Copyright (c) 2021 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.payment.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.payment.dto.AlipayNotifyLogDTO;
import io.boot.payment.entity.AlipayNotifyLogEntity;

/**
 * 支付宝回调日志
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface AlipayNotifyLogService extends CrudService<AlipayNotifyLogEntity, AlipayNotifyLogDTO> {

}