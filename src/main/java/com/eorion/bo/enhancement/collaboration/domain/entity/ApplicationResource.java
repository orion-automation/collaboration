package com.eorion.bo.enhancement.collaboration.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.eorion.bo.enhancement.collaboration.domain.enums.ApplicationResStatus;
import com.eorion.bo.enhancement.collaboration.domain.enums.ApplicationResourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@TableName(value = "COLLABORATION_APPLICATION_RESOURCE")
@EqualsAndHashCode(callSuper = true)
@ToString
public class ApplicationResource extends BaseEntity{

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "APPLICATION_ID")
    private Integer applicationId;

    @TableField(value = "NAME_TXT")
    private String name;

    @TableField(value = "TYPE_FG")
    private ApplicationResourceType type;

    @TableField(value = "ICON_URL")
    private String icon;

    @TableField(value = "PARENT_NODE")
    private Integer parentNode;

    @TableField(value = "EXTERNAL_RESOURCE_ID")
    private String externalResourceId;

    @TableField(value = "LINKED_RESOURCE_ID")
    private String linkedResourceId;

    @TableField(value = "EDIT_GROUP_TXT")
    private String editGroup;

    @TableField(value = "STATUS_TXT")
    private ApplicationResStatus status;

    @TableField(value = "DEF_KEY")
    private String definitionKey;

    @TableField(value = "DEF_ID")
    private String definitionId;

    @TableField(value = "CONFIG_JSON_TXT")
    private String configJson;
}
