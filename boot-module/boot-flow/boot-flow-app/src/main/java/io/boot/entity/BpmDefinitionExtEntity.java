package io.boot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.boot.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 流程定义扩展
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bpm_definition_ext")
public class BpmDefinitionExtEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 模型ID
     */
    private String modelId;
    /**
     * 流程定义ID
     */
    private String processDefinitionId;
    /**
     * 表单类型
     */
    private String formType;
    /**
     * 表单ID
     */
    private String formId;
    /**
     * 表单内容
     */
    private String formContent;
    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}