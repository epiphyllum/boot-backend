/**
 * Copyright (c) 2021 人人开源 All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */
package io.boot.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.dto.SysPostDTO;
import io.boot.entity.SysPostEntity;

/**
 * 岗位管理
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface SysPostService extends CrudService<SysPostEntity, SysPostDTO> {

}