/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.boot.entity.BaseClassEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基类管理
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface BaseClassDao extends BaseMapper<BaseClassEntity> {
	
}