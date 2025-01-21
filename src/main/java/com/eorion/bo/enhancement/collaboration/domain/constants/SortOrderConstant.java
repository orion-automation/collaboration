package com.eorion.bo.enhancement.collaboration.domain.constants;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum SortOrderConstant {

    DESC("desc", "DESC"),
    ASC("asc", "ASC");

    private static final Map<String, SortOrderConstant> BY_VALUE = new HashMap<>();

    static {
        for (SortOrderConstant e : values()) {
            BY_VALUE.put(e.key, e);
        }
    }

    private final String key;
    private final String value;

    SortOrderConstant(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static SortOrderConstant from(String s) {
        return BY_VALUE.get(s);
    }
}
