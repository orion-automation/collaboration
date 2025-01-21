package com.eorion.bo.enhancement.collaboration.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.eorion.bo.enhancement.collaboration.domain.enums.ResourceAssociationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@TableName(value = "COLLABORATION_COOPERATION_RES_ASSOCIATION")
@Data
public class Association {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "RESOURCE_ID")
    private Long resourceId;

    @TableField(value = "NAME_TXT")
    private String name;

    @TableField(value = "URL_TXT")
    private String url;

    @TableField(value = "TYPE_FG")
    private ResourceAssociationType type;

    @TableField(value = "CREATED_TS", fill = FieldFill.INSERT)
    private Long createdTs;

    @TableField(value = "UPDATED_TS", fill = FieldFill.INSERT_UPDATE)
    private Long updatedTs;

    @TableField(value = "CREATED_BY", fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(value = "UPDATED_BY", fill = FieldFill.UPDATE)
    private String updatedBy;

    @TableField(value = "DELETE_FG")
    @TableLogic(value = "0", delval = "1")
    @JsonIgnore
    private short deleteFlag;
}
