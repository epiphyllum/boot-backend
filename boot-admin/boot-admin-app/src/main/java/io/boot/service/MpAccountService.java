package io.boot.service;


import io.boot.commons.mybatis.service.CrudService;
import io.boot.dto.MpAccountDTO;
import io.boot.entity.MpAccountEntity;

/**
 * 公众号账号管理
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface MpAccountService extends CrudService<MpAccountEntity, MpAccountDTO> {

}