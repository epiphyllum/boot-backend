/**
 * Copyright (c) 2021 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.dto.WeChatNotifyLogDTO;
import io.boot.entity.WeChatNotifyLogEntity;

/**
 * 微信支付回调日志
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface WeChatNotifyLogService extends CrudService<WeChatNotifyLogEntity, WeChatNotifyLogDTO> {

}