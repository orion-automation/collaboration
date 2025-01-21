package com.eorion.bo.enhancement.collaboration.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.eorion.bo.enhancement.collaboration.domain.enums.RoleStatus;
import lombok.Data;


@Data
@TableName("COLLABORATION_COOPERATION_MEMBER")
public class Member {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "PROJECT_ID")
    private Integer projectId;

    @TableField(value = "USER_ID")
    private String userId;

    @TableField(value = "NAME_TXT")
    private String name;

    // comment '角色（1=owner, 2=管理员，3=编辑，4=评论员）'
    @TableField(value = "ROLE")
    private RoleStatus role;

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
}
