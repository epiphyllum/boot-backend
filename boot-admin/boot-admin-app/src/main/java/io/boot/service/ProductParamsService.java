package io.boot.service;


import io.boot.commons.mybatis.service.BaseService;
import io.boot.dto.ProductParamsDTO;
import io.boot.entity.ProductParamsEntity;

import java.util.List;

/**
 * 产品参数管理
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface ProductParamsService extends BaseService<ProductParamsEntity> {

    void saveOrUpdate(Long productId, List<ProductParamsDTO> list);

    void deleteByProductIds(Long[] productIds);

    List<ProductParamsDTO> getList(Long productId);
}