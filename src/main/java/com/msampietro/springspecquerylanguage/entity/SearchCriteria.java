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

    public SearchCriteria(final String orPredicate, final String key, final SearchOperation operation, final Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.orPredicate = orPredicate != null && orPredicate.equals(SearchOperation.OR_PREDICATE_FLAG);
        if (value instanceof String && StringUtils.contains(value.toString(), EQUAL))
            this.value = parseValue();
    }

    public boolean isOrPredicate() {
        return this.orPredicate;
    }

    private SearchCriteria parseValue() {
        String[] parsedString = StringUtils.split(this.value.toString(), EQUAL);
        return new SearchCriteria(null, parsedString[0], null, parsedString[1]);
    }

    public boolean isValueSearchCriteria() {
        return this.value instanceof SearchCriteria;
    }

}
