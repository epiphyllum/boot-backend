/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.boot.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.rbac.service;

import io.boot.commons.mybatis.service.BaseService;
import io.boot.commons.tools.page.PageData;
import io.boot.rbac.dto.NewsDTO;
import io.boot.rbac.entity.NewsEntity;

import java.util.Map;

/**
 * 新闻
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface NewsService extends BaseService<NewsEntity> {

    PageData<NewsDTO> page(Map<String, Object> params);

    NewsDTO get(Long id);

    void save(NewsDTO dto);

    void update(NewsDTO dto);
}

