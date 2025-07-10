/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.boot.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.rbac.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.boot.rbac.excel.converter.SysLogOperationConverter;
import lombok.Data;

import java.util.Date;

/**
 * 登录日志
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SysLogLoginExcel {
    @ExcelProperty(value = "操作类型", converter = SysLogOperationConverter.class)
    private Integer operation;

    @ExcelProperty("User-Agent")
    private String userAgent;

    @ExcelProperty("操作IP")
    private String ip;

    @ExcelProperty("用户名")
    private String creatorName;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("创建时间")
    private Date createDate;

}
