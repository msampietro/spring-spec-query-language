package com.msampietro.springspecquerylanguage.entity;

import java.util.Map;

import static com.msampietro.springspecquerylanguage.misc.Constants.*;

public enum SearchOperation {
    EQUALITY, NEGATION, GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL, LIKE, COLLECTION_CONTAINS, STARTS_WITH, ENDS_WITH, CONTAINS;

    private static final Map<String, SearchOperation> SEARCH_KEY_OPERATION_MAP;
    private static final String[] SIMPLE_OPERATION_SET = {COLON, CLOSING_EXCLAMATION, GREATER_THAN_SIGN, LESS_THAN_SIGN, TILDE, DASH};
    public static final String OPERATION_EXPRESSION = String.join("|", SearchOperation.SIMPLE_OPERATION_SET);
    public static final String OR_PREDICATE_FLAG = "'";
    public static final String REGEX_PREFIX = "(\\w+?)(";
    public static final String REGEX_SIMPLE_SUFFIX = ")(\\p{Punct}?)([\\p{L}\\p{Digit}\\/\\p{Space}-:\\(]+?)(\\p{Punct}?)";
    public static final String REGEX_NESTED_SUFFIX = ")(\\{)(\\w+?)(=)(\\p{Punct}?)([\\p{L}\\p{Digit}\\/\\p{Space}-:\\(]+?)(\\p{Punct}?)(})";

    static {
        SEARCH_KEY_OPERATION_MAP = Map.of(COLON, EQUALITY,
                CLOSING_EXCLAMATION, NEGATION,
                GREATER_THAN_SIGN, GREATER_THAN_OR_EQUAL,
                LESS_THAN_SIGN, LESS_THAN_OR_EQUAL,
                TILDE, LIKE,
                DASH, COLLECTION_CONTAINS);
    }

    public static SearchOperation getSimpleOperation(String input) {
        return SEARCH_KEY_OPERATION_MAP.get(input);
    }

}