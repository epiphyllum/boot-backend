/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.oss.service;

import io.boot.commons.mybatis.service.BaseService;
import io.boot.commons.tools.page.PageData;
import io.boot.oss.entity.OssEntity;

import java.util.Map;

/**
 * 文件上传
 * 
 * @author epiphyllum.zhou@gmail.com
 */
public interface OssService extends BaseService<OssEntity> {

	PageData<OssEntity> page(Map<String, Object> params);
}
