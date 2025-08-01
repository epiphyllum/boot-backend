/**
 * Copyright (c) 2019 BootCloud All rights reserved.
 *
 * https://www.boot.io
 *
 * 版权所有，侵权必究！
 */
package io.boot.rbac.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.rbac.entity.SysNoticeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 通知管理
*
* @author Mark sunlightcs@gmail.com
*/
@Mapper
public interface SysNoticeDao extends BaseDao<SysNoticeEntity> {
    /**
     * 获取被通知的用户列表
     */
    List<SysNoticeEntity> getNoticeUserList(Map<String, Object> params);

    /**
     * 获取我的通知列表
     */
    List<SysNoticeEntity> getMyNoticeList(Map<String, Object> params);
}