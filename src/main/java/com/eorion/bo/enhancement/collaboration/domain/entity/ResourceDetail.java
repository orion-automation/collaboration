package com.eorion.bo.enhancement.collaboration.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@TableName(value = "COLLABORATION_COOPERATION_RES_DETAIL")
@Data
public class ResourceDetail {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("RESOURCE_ID")
    private Long resourceId;

    @TableField(value = "NAME_TXT")
    private String name;

    @TableField(value = "XML_")
    private String xml;

    @TableField(value = "CONFIG_JSON_TXT")
    private String configJson;

    @TableField(value = "PASSWORD_TXT")
    private String password;

    @TableField(value = "VERSION")
    private Integer version;

    @TableField(value = "ZERO_CODE_EFFORT_TIME")
    private Long zeroCodeEffort;

    @TableField(value = "LOW_CODE_EFFORT_TIME")
    private Long lowCodeEffort;

    @TableField(value = "ADVANCE_CODE_EFFORT_TIME")
    private Long advanceCodeEffort;

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
    private short deleteFlag;

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }
}
