package io.boot.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.dto.BpmFormDTO;
import io.boot.entity.BpmFormEntity;

/**
 * 工作流表单
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface BpmFormService extends CrudService<BpmFormEntity, BpmFormDTO> {

}