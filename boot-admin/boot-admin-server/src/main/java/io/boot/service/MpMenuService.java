package io.boot.service;


import io.boot.commons.mybatis.service.CrudService;
import io.boot.dto.MpMenuDTO;
import io.boot.entity.MpMenuEntity;

/**
 * 公众号自定义菜单
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface MpMenuService extends CrudService<MpMenuEntity, MpMenuDTO> {

    MpMenuDTO getByAppId(String appId);

    void deleteByAppId(String appId);
}