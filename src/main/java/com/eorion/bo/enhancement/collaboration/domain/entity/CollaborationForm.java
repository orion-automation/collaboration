package com.eorion.bo.enhancement.collaboration.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "COLLABORATION_FORM")
@ToString
public class CollaborationForm extends BaseEntity {

    //表单id
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    //表单名称
    @TableField(value = "NAME_TXT")
    private String name;

    //表单定义key
    @TableField(value = "DEFINITION_KEY_TXT")
    private String definitionKey;

    //类型
    @TableField(value = "TYPE_TXT")
    private String type;

    //表单数据
    @TableField(value = "FROM_DATA")
    private String formData;

    @TableField(value = "TENANT_TXT")
    private String tenant;

}
