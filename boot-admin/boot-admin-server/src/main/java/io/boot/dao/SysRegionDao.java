package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.SysRegionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 行政区域
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface SysRegionDao extends BaseDao<SysRegionEntity> {

	List<SysRegionEntity> getList(Map<String, Object> params);

	List<SysRegionEntity> getListByLevel(Integer treeLevel);

	List<Map<String, Object>> getTreeList();

	SysRegionEntity getById(Long id);

	int getCountByPid(Long pid);

}