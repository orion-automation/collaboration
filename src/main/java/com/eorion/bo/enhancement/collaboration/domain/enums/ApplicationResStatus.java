package com.eorion.bo.enhancement.collaboration.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApplicationResStatus {
    DRAFT("0", "草稿"),
    PUBLISH("1", "上架"),
    OFFLINE("2", "下架");

    private static final Map<String, ApplicationResStatus> BY_VALUE = new HashMap<>();

    static {
        for (ApplicationResStatus e : values()) {
            BY_VALUE.put(e.value, e);
        }
    }

    @EnumValue
    private final String value;
    private final String desc;

    ApplicationResStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator
    public static ApplicationResStatus from(String s) {
        return BY_VALUE.get(s);
    }
}
