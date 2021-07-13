package com.msampietro.springspecquerylanguage.entity;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import static com.msampietro.springspecquerylanguage.misc.Constants.EQUAL;

@Getter
public class SearchCriteria {
    private final String key;
    private final SearchOperation operation;
    private Object value;
    private final boolean orPredicate;

    public SearchCriteria(final boolean isOrPredicate, final String key, final SearchOperation operation, final Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.orPredicate = isOrPredicate;
        if (shouldParseValue(value))
            this.value = parseValue();
    }

    private boolean shouldParseValue(Object value) {
        return value instanceof String && StringUtils.contains(value.toString(), EQUAL);
    }

    public boolean isOrPredicate() {
        return this.orPredicate;
    }

    private SearchCriteria parseValue() {
        String[] parsedString = StringUtils.split(this.value.toString(), EQUAL);
        return new SearchCriteria(false, parsedString[0], null, parsedString[1]);
    }

    public boolean isValueSearchCriteria() {
        return this.value instanceof SearchCriteria;
    }

}
