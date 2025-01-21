package com.eorion.bo.enhancement.collaboration.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResourceAssociationType {

    RUNTIME("1", "运行时"),
    KNOWLEDGE("2", "知识库");
    private static final Map<String, ResourceAssociationType> BY_VALUE = new HashMap<>();

    static {
        for (ResourceAssociationType e : values()) {
            BY_VALUE.put(e.value, e);
        }
    }

    @EnumValue
    private final String value;
    private final String desc;

    ResourceAssociationType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator
    public static ResourceAssociationType from(String s) {
        return BY_VALUE.get(s);}
}
