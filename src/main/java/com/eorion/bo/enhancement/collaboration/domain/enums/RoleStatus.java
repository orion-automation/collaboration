package com.eorion.bo.enhancement.collaboration.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum RoleStatus {

    OWNER("1", "OWNER"),
    MANGER("2", "管理员"),
    EDIT("3", "编辑者"),
    COMMENT("4", "评论员");

    private static final Map<String, RoleStatus> BY_VALUE = new HashMap<>();

    static {
        for (RoleStatus e : values()) {
            BY_VALUE.put(e.value, e);
        }
    }

    @EnumValue
    private final String value;
    private final String desc;

    RoleStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator
    public static RoleStatus from(String s) {
        return BY_VALUE.get(s);
    }
}
