/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.message.service;

import io.boot.commons.mybatis.service.BaseService;
import io.boot.commons.tools.page.PageData;
import io.boot.message.dto.SysMailLogDTO;
import io.boot.message.entity.SysMailLogEntity;

import java.util.Map;

/**
 * 邮件发送记录
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface SysMailLogService extends BaseService<SysMailLogEntity> {

    PageData<SysMailLogDTO> page(Map<String, Object> params);

    /**
     * 保存邮件发送记录
     * @param templateId  模板ID
     * @param from        发送者
     * @param to          收件人
     * @param cc          抄送
     * @param subject     主题
     * @param content     邮件正文
     * @param status      状态
     */
    void save(Long templateId, String from, String[] to, String[] cc, String subject, String content, Integer status);
}

