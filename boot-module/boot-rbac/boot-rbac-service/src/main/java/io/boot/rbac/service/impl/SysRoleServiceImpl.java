/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.boot.commons.mybatis.enums.DelFlagEnum;
import io.boot.commons.mybatis.service.impl.BaseServiceImpl;
import io.boot.commons.security.user.SecurityUser;
import io.boot.commons.security.user.UserDetail;
import io.boot.commons.tools.constant.Constant;
import io.boot.commons.tools.enums.SuperAdminEnum;
import io.boot.commons.tools.page.PageData;
import io.boot.commons.tools.utils.ConvertUtils;
import io.boot.rbac.dao.SysRoleDao;
import io.boot.rbac.dto.SysRoleDTO;
import io.boot.rbac.entity.SysRoleEntity;
import io.boot.rbac.service.*;
import io.boot.rbac.service.*;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
    @Resource
    private SysRoleUserService sysRoleUserService;
    @Resource
    private SysRoleMenuService sysRoleMenuService;
    @Resource
    private SysRoleDataScopeService sysRoleDataScopeService;
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysUserTokenService sysUserTokenService;

    @Override
    public PageData<SysRoleDTO> page(Map<String, Object> params) {
        IPage<SysRoleEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, SysRoleDTO.class);
    }

    @Override
    public List<SysRoleDTO> list(Map<String, Object> params) {
        List<SysRoleEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SysRoleDTO.class);
    }

    private QueryWrapper<SysRoleEntity> getWrapper(Map<String, Object> params) {
        String name = (String) params.get("name");

        QueryWrapper<SysRoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.like(StringUtils.isNotBlank(name), "name", name);

        //普通管理员，只能查询所属部门及子部门的数据
        UserDetail user = SecurityUser.getUser();
        if (user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
            List<Long> deptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
            wrapper.in(deptIdList != null, "dept_id", deptIdList);
        }

        return wrapper;
    }

    @Override
    public SysRoleDTO get(Long id) {
        SysRoleEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SysRoleDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysRoleDTO dto) {
        SysRoleEntity entity = ConvertUtils.sourceToTarget(dto, SysRoleEntity.class);

        //保存角色
        insert(entity);

        //保存角色菜单关系
        sysRoleMenuService.saveOrUpdate(entity.getId(), dto.getMenuIdList());

        //保存角色数据权限关系
        sysRoleDataScopeService.saveOrUpdate(entity.getId(), dto.getDeptIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleDTO dto) {
        SysRoleEntity entity = ConvertUtils.sourceToTarget(dto, SysRoleEntity.class);

        //更新角色
        updateById(entity);

        //更新角色菜单关系
        sysRoleMenuService.saveOrUpdate(entity.getId(), dto.getMenuIdList());

        //更新角色数据权限关系
        sysRoleDataScopeService.saveOrUpdate(entity.getId(), dto.getDeptIdList());

        // 更新用户的缓存权限
        sysUserTokenService.updateCacheAuthByRoleId(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        //逻辑删除角色
        logicDelete(ids, SysRoleEntity.class);

        //删除角色用户关系
        sysRoleUserService.deleteByRoleIds(ids);

        //[角色菜单关系、角色数据权限关系]，需要保留，不然逻辑删除就变成物理删除了

        // 更新用户的缓存权限
        Arrays.stream(ids).toList().forEach(sysUserTokenService::updateCacheAuthByRoleId);
    }

}