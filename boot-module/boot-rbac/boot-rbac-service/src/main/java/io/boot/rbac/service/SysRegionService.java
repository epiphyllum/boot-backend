/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.boot.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.rbac.service;

import io.boot.commons.mybatis.service.BaseService;
import io.boot.rbac.dto.SysRegionDTO;
import io.boot.rbac.dto.region.RegionProvince;
import io.boot.rbac.entity.SysRegionEntity;

import java.util.List;
import java.util.Map;

/**
 * 行政区域
 * 
 * @author Mark sunlightcs@gmail.com
 */
public interface SysRegionService extends BaseService<SysRegionEntity> {

	List<SysRegionDTO> list(Map<String, Object> params);

	List<Map<String, Object>> getTreeList();

	SysRegionDTO get(Long id);

	void save(SysRegionDTO dto);

	void update(SysRegionDTO dto);

	void delete(Long id);

	int getCountByPid(Long pid);

	List<RegionProvince> getRegion(boolean threeLevel);
}