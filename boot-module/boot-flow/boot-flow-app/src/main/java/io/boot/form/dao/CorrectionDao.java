/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.form.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.form.entity.CorrectionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 转正申请
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface CorrectionDao extends BaseDao<CorrectionEntity> {

}