package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.SysUserTokenEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户Token
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysUserTokenDao extends BaseDao<SysUserTokenEntity> {

    /**
     * 根据角色ID，查询在线用户 access_token 列表
     *
     * @param roleId 角色ID
     * @param time   当前时间
     * @return 返回 access_token 列表
     */
    List<String> getOnlineAccessTokenListByRoleId(@Param("roleId") Long roleId, @Param("time") Date time);

    /**
     * 根据用户ID，查询在线用户 access_token 列表
     *
     * @param userId 用户ID
     * @param time   当前时间
     * @return 返回 access_token 列表
     */
    List<String> getOnlineAccessTokenListByUserId(@Param("userId") Long userId, @Param("time") Date time);

}