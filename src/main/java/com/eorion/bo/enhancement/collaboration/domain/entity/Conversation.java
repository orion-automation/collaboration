package com.eorion.bo.enhancement.collaboration.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

@TableName(value = "COLLABORATION_COOPERATION_NODE_CONVERSATION")
@Data
public class Conversation {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     对应不同版本资源详情下的节点 ID
     */
    @TableField(value = "NODE_ID")
    private Long nodeId;

    @TableField(value = "MESSAGE_TXT")
    private String message;

    @TableField(value = "CREATED_TS", fill = FieldFill.INSERT)
    private Long createdTs;

    @TableField(value = "UPDATED_TS", fill = FieldFill.INSERT_UPDATE)
    private Long updatedTs;

    @TableField(value = "CREATED_BY", fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(value = "CREATED_BY", fill = FieldFill.UPDATE)
    private String updatedBy;

    @TableField(value = "DELETE_FG")
    @TableLogic(value = "0", delval = "1")
    @JsonIgnore
    private short deleteFlag;

    public Conversation(Long nodeId, String message) {
        this.nodeId = nodeId;
        this.message = message;
    }

    public Conversation() {
    }
}
