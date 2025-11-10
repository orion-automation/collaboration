package com.eorion.bo.enhancement.collaboration.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ResourceType {

    FOLDER("1", "folder"),
    BPMN("2", "bpmn"),
    DMN("3", "dmn"),
    FORM("4", "form"),
    PAGE("5", "page"),
    UX("6", "ux"),
    DATA("7", "data"),
    DOC("8", "doc"),;

    private static final Map<String, ResourceType> BY_VALUE = new HashMap<>();

    static {
        for (ResourceType e : values()) {
            BY_VALUE.put(e.value, e);
        }
    }

    @EnumValue
    private final String value;
    private final String desc;

    ResourceType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator
    public static ResourceType from(String s) {
        return BY_VALUE.get(s);
    }
}
