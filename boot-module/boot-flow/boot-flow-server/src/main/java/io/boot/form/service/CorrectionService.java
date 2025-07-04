/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.form.service;

import io.boot.commons.mybatis.service.BaseService;
import io.boot.form.dto.CorrectionDTO;
import io.boot.form.entity.CorrectionEntity;

/**
 * 转正申请
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface CorrectionService extends BaseService<CorrectionEntity> {

    CorrectionDTO get(String instanceId);

    void save(CorrectionDTO dto);
}