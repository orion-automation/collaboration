package com.eorion.bo.enhancement.collaboration.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class BaseEntity {

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
    @JsonIgnore
    private short deleteFlag;
}
