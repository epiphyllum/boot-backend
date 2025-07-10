/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.form.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.boot.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 转正申请（自定义表单）
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bpm_form_correction")
public class CorrectionEntity extends BaseEntity {
    /**
     * 申请岗位
     */
    private String applyPost;
    /**
     * 入职日期
     */
    private Date entryDate;
    /**
     * 转正日期
     */
    private Date correctionDate;
    /**
     * 工作内容
     */
    private String workContent;
    /**
     * 工作成绩
     */
    private String achievement;
    /**
     * 流程实例ID
     */
    private String instanceId;
}