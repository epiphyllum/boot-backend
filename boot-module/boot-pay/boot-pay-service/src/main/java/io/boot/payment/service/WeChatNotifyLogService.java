/**
 * Copyright (c) 2021 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.payment.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.payment.dto.WeChatNotifyLogDTO;
import io.boot.payment.entity.WeChatNotifyLogEntity;

/**
 * 微信支付回调日志
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface WeChatNotifyLogService extends CrudService<WeChatNotifyLogEntity, WeChatNotifyLogDTO> {

}