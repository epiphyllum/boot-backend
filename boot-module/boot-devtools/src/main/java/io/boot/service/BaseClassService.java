/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.service;


import io.boot.commons.mybatis.service.BaseService;
import io.boot.commons.tools.page.PageData;
import io.boot.entity.BaseClassEntity;

import java.util.List;
import java.util.Map;

/**
 * 基类管理
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface BaseClassService extends BaseService<BaseClassEntity> {

    PageData<BaseClassEntity> page(Map<String, Object> params);

    List<BaseClassEntity> list();
}