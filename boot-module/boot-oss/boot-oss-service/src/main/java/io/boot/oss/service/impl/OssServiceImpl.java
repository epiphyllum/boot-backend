/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.boot.commons.mybatis.service.impl.BaseServiceImpl;
import io.boot.commons.tools.constant.Constant;
import io.boot.commons.tools.page.PageData;
import io.boot.oss.dao.OssDao;
import io.boot.oss.entity.OssEntity;
import io.boot.oss.service.OssService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OssServiceImpl extends BaseServiceImpl<OssDao, OssEntity> implements OssService {

	@Override
	public PageData<OssEntity> page(Map<String, Object> params) {
		IPage<OssEntity> page = baseDao.selectPage(
			getPage(params, Constant.CREATE_DATE, false),
			new QueryWrapper<>()
		);
		return getPageData(page, OssEntity.class);
	}
}
