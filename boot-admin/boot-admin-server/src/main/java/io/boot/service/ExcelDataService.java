package io.boot.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.dto.ExcelDataDTO;
import io.boot.entity.ExcelDataEntity;

/**
 * Excel导入演示
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface ExcelDataService extends CrudService<ExcelDataEntity, ExcelDataDTO> {

}