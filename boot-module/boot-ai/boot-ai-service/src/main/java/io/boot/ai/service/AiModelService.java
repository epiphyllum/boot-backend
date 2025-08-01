package io.boot.ai.service;


import io.boot.commons.mybatis.service.BaseService;
import io.boot.commons.tools.page.PageData;
import io.boot.ai.dto.AiModelDTO;
import io.boot.ai.entity.AiModelEntity;

import java.util.List;
import java.util.Map;

/**
 * AI模型
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface AiModelService extends BaseService<AiModelEntity> {

    PageData<AiModelDTO> page(Map<String, Object> params);

    List<AiModelDTO> getList();

    AiModelDTO get(Long id);

    void save(AiModelDTO dto);

    void update(AiModelDTO dto);

    void delete(List<Long> idList);

}