package com.eorion.bo.enhancement.collaboration.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProcessDevTimeType {

    ZERO("zero", "ZERO_CODE_EFFORT_TIME"),
    LOW("low", "LOW_CODE_EFFORT_TIME"),
    ADVANCE("advance", "ADVANCE_CODE_EFFORT_TIME");

    private static final Map<String, ProcessDevTimeType> BY_VALUE = new HashMap<>();

    static {
        for (ProcessDevTimeType e : values()) {
            BY_VALUE.put(e.value, e);
        }
    }

    @EnumValue
    private final String value;
    private final String desc;

    ProcessDevTimeType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    @JsonCreator
    public static ProcessDevTimeType from(String s) {
        return BY_VALUE.get(s);
    }
}
