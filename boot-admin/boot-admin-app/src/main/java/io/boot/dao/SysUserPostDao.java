/**
 * Copyright (c) 2021 BootCloud All rights reserved.
 *
 * https://www.boot.io
 *
 * 版权所有，侵权必究！
 */
package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.SysUserPostEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 用户岗位关系
*
* @author Mark sunlightcs@gmail.com
*/
@Mapper
public interface SysUserPostDao extends BaseDao<SysUserPostEntity> {

    /**
     * 根据岗位ids，删除岗位用户关系
     * @param postIds 岗位ids
     */
    void deleteByPostIds(Long[] postIds);

    /**
     * 根据用户id，删除岗位用户关系
     * @param userIds 用户ids
     */
    void deleteByUserIds(Long[] userIds);

    /**
     * 岗位ID列表
     * @param userId  用户ID
     */
    List<Long> getPostIdList(Long userId);
}