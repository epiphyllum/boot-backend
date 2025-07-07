/**
 * Copyright (c) 2021 人人开源 All rights reserved.
 *
 * https://www.boot.io
 *
 * 版权所有，侵权必究！
 */
package io.boot.rbac.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.rbac.dto.SysPostDTO;
import io.boot.rbac.entity.SysPostEntity;

/**
 * 岗位管理
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysPostService extends CrudService<SysPostEntity, SysPostDTO> {

}