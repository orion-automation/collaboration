package com.eorion.bo.enhancement.collaboration.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@TableName(value = "COLLABORATION_COOPERATION_BPMN_NODE")
@Data
public class ResourceBpmnNode {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "RESOURCE_DETAIL_ID")
    private String resourceDetailId;

    @TableField(value = "ACTIVITY_ID")
    private String activityId;

    @TableField(value = "DELETE_FG")
    @TableLogic(value = "0", delval = "1")
    private short deleteFlag;
}
