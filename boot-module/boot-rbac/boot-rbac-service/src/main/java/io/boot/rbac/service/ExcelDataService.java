package io.boot.rbac.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.rbac.dto.ExcelDataDTO;
import io.boot.rbac.entity.ExcelDataEntity;

/**
 * Excel导入演示
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface ExcelDataService extends CrudService<ExcelDataEntity, ExcelDataDTO> {

}