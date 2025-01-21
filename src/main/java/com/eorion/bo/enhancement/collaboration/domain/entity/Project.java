package com.eorion.bo.enhancement.collaboration.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.eorion.bo.enhancement.collaboration.domain.enums.ProjectType;
import lombok.Data;

@Data
@TableName(value = "COLLABORATION_COOPERATION_PROJECT")
public class Project {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "NAME_TXT")
    private String name;

    @TableField(value = "COE_CODE")
    private String coeCode;

    @TableField(value = "TAGS_TXT")
    private String tags;

    @TableField(value = "OWNER")
    private String owner;

    @TableField(value = "TENANT")
    private String tenant;

    @TableField(value = "TYPE_TXT")
    private ProjectType type;

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

    @TableField(value = "CONFIG_JSON_TXT")
    private String configJson;
}
