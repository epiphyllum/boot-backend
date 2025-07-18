package io.boot.mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.boot.commons.mybatis.service.impl.CrudServiceImpl;
import io.boot.mp.dao.MpAccountDao;
import io.boot.mp.dto.MpAccountDTO;
import io.boot.mp.entity.MpAccountEntity;
import io.boot.mp.service.MpAccountService;
import io.boot.mp.service.MpAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 公众号账号管理
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Service
public class MpAccountServiceImpl extends CrudServiceImpl<MpAccountDao, MpAccountEntity, MpAccountDTO> implements MpAccountService {

    @Override
    public QueryWrapper<MpAccountEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<MpAccountEntity> wrapper = new QueryWrapper<>();

        String name = (String)params.get("name");
        wrapper.like(StringUtils.isNotBlank(name), "name", name);

        String appId = (String)params.get("appId");
        wrapper.like(StringUtils.isNotBlank(appId), "app_id", appId);

        return wrapper;
    }

}