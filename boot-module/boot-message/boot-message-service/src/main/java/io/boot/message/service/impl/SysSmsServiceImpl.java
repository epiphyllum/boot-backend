/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */
package io.boot.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.boot.commons.mybatis.service.impl.CrudServiceImpl;
import io.boot.commons.tools.exception.ErrorCode;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.ConvertUtils;
import io.boot.commons.tools.utils.JsonUtils;
import io.boot.message.dao.SysSmsDao;
import io.boot.message.dto.SmsConfig;
import io.boot.message.dto.SysSmsDTO;
import io.boot.message.entity.SysSmsEntity;
import io.boot.message.exception.ModuleErrorCode;
import io.boot.message.service.SysSmsService;
import io.boot.message.sms.AbstractSmsService;
import io.boot.message.sms.SmsFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class SysSmsServiceImpl extends CrudServiceImpl<SysSmsDao, SysSmsEntity, SysSmsDTO> implements SysSmsService {

    @Override
    public QueryWrapper<SysSmsEntity> getWrapper(Map<String, Object> params){
        String platform = (String)params.get("platform");

        QueryWrapper<SysSmsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(platform), "platform", platform);

        return wrapper;
    }

    @Override
    public SysSmsDTO get(Long id) {
        SysSmsEntity entity = baseDao.selectById(id);

        SysSmsDTO dto = ConvertUtils.sourceToTarget(entity, SysSmsDTO.class);
        dto.setConfig(JsonUtils.parseObject(entity.getSmsConfig(), SmsConfig.class));

        return dto;
    }

    @Override
    public void send(String smsCode, String mobile, String params) {
        LinkedHashMap<String, String> map;
        try {
            map = JsonUtils.parseObject(params, LinkedHashMap.class);
        }catch (Exception e){
            throw new BootException(ErrorCode.JSON_FORMAT_ERROR);
        }

        //短信服务
        AbstractSmsService service = SmsFactory.build(smsCode);
        if(service == null){
            throw new BootException(ModuleErrorCode.SMS_CONFIG);
        }

        //发送短信
        service.sendSms(smsCode, mobile, map);
    }

    @Override
    public SysSmsEntity getBySmsCode(String smsCode) {
        QueryWrapper<SysSmsEntity> query = new QueryWrapper<>();
        query.eq("sms_code", smsCode);

        return baseDao.selectOne(query);
    }

    @Override
    public void save(SysSmsDTO dto) {
        SysSmsEntity entity = ConvertUtils.sourceToTarget(dto, SysSmsEntity.class);
        entity.setSmsConfig(JsonUtils.toJsonString(dto.getConfig()));
        baseDao.insert(entity);
    }

    @Override
    public void update(SysSmsDTO dto) {
        SysSmsEntity entity = ConvertUtils.sourceToTarget(dto, SysSmsEntity.class);
        entity.setSmsConfig(JsonUtils.toJsonString(dto.getConfig()));
        baseDao.updateById(entity);
    }
}
