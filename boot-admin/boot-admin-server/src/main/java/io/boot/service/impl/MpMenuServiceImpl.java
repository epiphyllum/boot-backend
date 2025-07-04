package io.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.boot.commons.mybatis.service.impl.CrudServiceImpl;
import io.boot.commons.tools.utils.ConvertUtils;
import io.boot.dao.MpMenuDao;
import io.boot.dto.MpMenuDTO;
import io.boot.entity.MpMenuEntity;
import io.boot.service.MpMenuService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 公众号自定义菜单
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Service
public class MpMenuServiceImpl extends CrudServiceImpl<MpMenuDao, MpMenuEntity, MpMenuDTO> implements MpMenuService {

    @Override
    public QueryWrapper<MpMenuEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<MpMenuEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

    @Override
    public MpMenuDTO getByAppId(String appId) {
        MpMenuEntity entity = baseDao.selectOne(new QueryWrapper<MpMenuEntity>().eq("app_id", appId));
        return ConvertUtils.sourceToTarget(entity, MpMenuDTO.class);
    }

    @Override
    public void deleteByAppId(String appId) {
        baseDao.delete(new QueryWrapper<MpMenuEntity>().eq("app_id", appId));
    }
}