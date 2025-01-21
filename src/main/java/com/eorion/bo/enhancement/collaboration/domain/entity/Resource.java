package com.eorion.bo.enhancement.collaboration.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.eorion.bo.enhancement.collaboration.domain.enums.CoopResourceStatus;
import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceType;
import lombok.Data;

@Data
@TableName("COLLABORATION_COOPERATION_RESOURCE")
public class Resource {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "PROJECT_ID")
    private Integer projectId;

    @TableField(value = "RESOURCE_NAME")
    private String name;

    @TableField(value = "TYPE")
    private ResourceType type;

    @TableField(value = "PARENT_NODE")
    private Integer parentNode;

    @TableField(value = "EXTERNAL_RESOURCE_ID")
    private String externalResourceId;

    @TableField(value = "CREATED_TS", fill = FieldFill.INSERT)
    private Long createdTs;

    @TableField(value = "UPDATED_TS", fill = FieldFill.INSERT_UPDATE)
    private Long updatedTs;

    @TableField(value = "CREATE_BY_TXT", fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(value = "UPDATED_BY_TXT", fill = FieldFill.UPDATE)
    private String updatedBy;

    @TableField(value = "DELETE_FG")
    @TableLogic(value = "0", delval = "1")
    private short deleteFlag;

    @TableField(value = "STATUS_TXT")
    private CoopResourceStatus status;

    @TableField(value = "TAGS_TXT")
    private String tags;

    @TableField(value = "CONFIG_JSON_TXT")
    private String configJson;


}
