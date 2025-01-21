package com.eorion.bo.enhancement.collaboration.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ApplicationResourceType {
    PAGE("1", "页面"),
    FORM("2", "表单"),
    UX("3", "ux流"),
    BUSINESS_FLOW("4", "业务流"),
    DATA_OBJECT("5", "数据对象");

    private static final Map<String, ApplicationResourceType> BY_VALUE = new HashMap<>();

    static {
        for (ApplicationResourceType e : values()) {
            BY_VALUE.put(e.value, e);
        }
    }

    @EnumValue
    private final String value;
    private final String desc;

    ApplicationResourceType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator
    public static ApplicationResourceType from(String s) {
        return BY_VALUE.get(s);
    }
}
