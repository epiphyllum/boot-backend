/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.sms;

import io.boot.commons.tools.utils.JsonUtils;
import io.boot.commons.tools.utils.SpringContextUtils;
import io.boot.dto.SmsConfig;
import io.boot.entity.SysSmsEntity;
import io.boot.enums.PlatformEnum;
import io.boot.service.SysSmsService;

/**
 * 短信Factory
 *
 * @author Mark sunlightcs@gmail.com
 */
public class SmsFactory {
    private static SysSmsService sysSmsService;

    static {
        SmsFactory.sysSmsService = SpringContextUtils.getBean(SysSmsService.class);
    }

    public static AbstractSmsService build(String smsCode){
        //获取短信配置信息
        SysSmsEntity smsEntity = sysSmsService.getBySmsCode(smsCode);
        SmsConfig config = JsonUtils.parseObject(smsEntity.getSmsConfig(), SmsConfig.class);

        if(smsEntity.getPlatform() == PlatformEnum.ALIYUN.value()){
            return new AliyunSmsService(config);
        }else if(smsEntity.getPlatform() == PlatformEnum.QCLOUD.value()){
            return new QcloudSmsService(config);
        }else if(smsEntity.getPlatform() == PlatformEnum.QINIU.value()){
            return new QiniuSmsService(config);
        }

        return null;
    }
}