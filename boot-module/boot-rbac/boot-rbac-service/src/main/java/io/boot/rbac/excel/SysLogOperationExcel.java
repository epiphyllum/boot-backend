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
import io.boot.rbac.excel.converter.SysLogStatusConverter;
import lombok.Data;

import java.util.Date;

/**
 * 操作日志
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SysLogOperationExcel {
    @ExcelProperty("用户操作")
    private String operation;

    @ExcelProperty("请求URI")
    private String requestUri;

    @ExcelProperty("请求方式")
    private String requestMethod;

    @ExcelProperty("请求参数")
    private String requestParams;

    @ExcelProperty("请求时长(毫秒)")
    private Integer requestTime;

    @ExcelProperty("User-Agent")
    private String userAgent;

    @ExcelProperty("操作IP")
    private String ip;

    @ExcelProperty(value = "状态", converter = SysLogStatusConverter.class)
    private Integer status;

    @ExcelProperty("用户名")
    private String creatorName;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("创建时间")
    private Date createDate;

}
