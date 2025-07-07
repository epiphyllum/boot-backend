package io.boot.rbac.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.rbac.dto.ProductDTO;
import io.boot.rbac.entity.ProductEntity;

/**
 * 产品管理
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface ProductService extends CrudService<ProductEntity, ProductDTO> {

}