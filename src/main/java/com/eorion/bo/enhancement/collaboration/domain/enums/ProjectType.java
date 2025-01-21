package com.eorion.bo.enhancement.collaboration.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ProjectType {

    PROCESS_DESIGN("1", "流程设计"),
    AI_TRAINING("2", "AI训练"),
    PROCESS_ATTACHMENTS("3", "流程拓展"),
    FORM("4", "表单"),
    PAGE("5", "页面");

    private static final Map<String, ProjectType> BY_VALUE = new HashMap<>();

    static {
        for (ProjectType e : values()) {
            BY_VALUE.put(e.value, e);
        }
    }

    @EnumValue
    private final String value;
    private final String desc;

    ProjectType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator
    public static ProjectType from(String s) {
        return BY_VALUE.get(s);
    }
}
