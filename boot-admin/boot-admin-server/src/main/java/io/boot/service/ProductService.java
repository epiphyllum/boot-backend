package io.boot.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.dto.ProductDTO;
import io.boot.entity.ProductEntity;

/**
 * 产品管理
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface ProductService extends CrudService<ProductEntity, ProductDTO> {

}