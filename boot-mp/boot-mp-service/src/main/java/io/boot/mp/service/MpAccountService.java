package io.boot.mp.service;


import io.boot.commons.mybatis.service.CrudService;
import io.boot.mp.dto.MpAccountDTO;
import io.boot.mp.entity.MpAccountEntity;

/**
 * 公众号账号管理
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface MpAccountService extends CrudService<MpAccountEntity, MpAccountDTO> {

}