package com.msampietro.springspecquerylanguage.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchSection {

    private final String key;
    private final String operation;
    private final Object value;
    private final String prefix;
    private final String suffix;
    private boolean isOrPredicate;

    public SearchSection(String key, String operation, Object value, String prefix, String suffix, boolean isOrPredicate) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.prefix = prefix;
        this.suffix = suffix;
        this.isOrPredicate = isOrPredicate;
    }

    public SearchSection(String key, String operation, Object value, String prefix, String suffix) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.prefix = prefix;
        this.suffix = suffix;
    }


}
