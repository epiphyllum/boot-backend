/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.boot.commons.mybatis.service.impl.BaseServiceImpl;
import io.boot.commons.tools.utils.ConvertUtils;
import io.boot.form.dao.CorrectionDao;
import io.boot.form.dto.CorrectionDTO;
import io.boot.form.entity.CorrectionEntity;
import io.boot.form.service.CorrectionService;
import io.boot.service.FlowService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 转正申请
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Service
@AllArgsConstructor
public class CorrectionServiceImpl extends BaseServiceImpl<CorrectionDao, CorrectionEntity> implements CorrectionService {
    private final FlowService flowService;

    @Override
    public CorrectionDTO get(String instanceId) {
        CorrectionEntity entity = baseDao.selectOne(new LambdaQueryWrapper<CorrectionEntity>().eq(CorrectionEntity::getInstanceId, instanceId));

        return ConvertUtils.sourceToTarget(entity, CorrectionDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CorrectionDTO dto) {
        CorrectionEntity entity = ConvertUtils.sourceToTarget(dto, CorrectionEntity.class);

        // 流程参数：条件流转等场景，需要把表单的值，放到流程参数里面；根据业务需要自行添加
        Map<String, Object> variables = new HashMap<>();

        // 启动流程
        String instanceId = flowService.startInstance(dto.getProcessDefinitionId(), variables, false);

        // 保存表单
        entity.setInstanceId(instanceId);
        insert(entity);
    }
}