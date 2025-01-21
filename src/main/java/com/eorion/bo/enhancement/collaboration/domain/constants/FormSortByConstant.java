package com.eorion.bo.enhancement.collaboration.domain.constants;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum FormSortByConstant {

    TYPE("type","TYPE_TXT"),
    TENANT("tenant", "TENANT_TXT"),
    CREATED_BY("createdBy", "CREATE_BY_TXT"),
    NAME("name", "NAME_TXT");

    private static final Map<String, FormSortByConstant> BY_VALUE = new HashMap<>();

    static {
        for (FormSortByConstant e : values()) {
            BY_VALUE.put(e.key, e);
        }
    }

    private final String key;
    private final String value;

    FormSortByConstant( String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static FormSortByConstant from(String s) {
        return BY_VALUE.get(s);
    }
}
