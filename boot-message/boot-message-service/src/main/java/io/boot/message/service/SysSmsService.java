/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.message.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.message.dto.SysSmsDTO;
import io.boot.message.entity.SysSmsEntity;

/**
 * 短信
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface SysSmsService extends CrudService<SysSmsEntity, SysSmsDTO> {

    /**
     * 发送短信
     * @param smsCode   短信编码
     * @param mobile   手机号
     * @param params   短信参数
     */
    void send(String smsCode, String mobile, String params);

    SysSmsEntity getBySmsCode(String smsCode);

}

