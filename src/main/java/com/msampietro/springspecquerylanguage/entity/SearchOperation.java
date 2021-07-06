package com.msampietro.springspecquerylanguage.entity;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.msampietro.springspecquerylanguage.misc.Constants.*;

public enum SearchOperation {
    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, COLLECTION_CONTAINS, STARTS_WITH, ENDS_WITH, CONTAINS;

    public static final String OR_PREDICATE_FLAG = "'";
    private static final String[] SIMPLE_OPERATION_SET = {":", "!", ">", "<", "~", "-"};
    public static final String OPERATION_EXPRESSION = String.join("|", SearchOperation.SIMPLE_OPERATION_SET);
    private static final String REGEX_PREFIX = "(\\w+?)(";
    private static final String REGEX_SIMPLE_SUFFIX = ")(\\p{Punct}?)([\\p{L}\\p{Digit}\\/\\p{Space}-:\\(]+?)(\\p{Punct}?)";
    public static final String SIMPLE_REGEX_PATTERN = StringUtils.join(REGEX_PREFIX, OPERATION_EXPRESSION, REGEX_SIMPLE_SUFFIX);
    private static final String REGEX_NESTED_SUFFIX = ")(\\{)(\\w+?)(=)(\\p{Punct}?)([\\p{L}\\p{Digit}\\/\\p{Space}-:\\(]+?)(\\p{Punct}?)(})";
    public static final String NESTED_REGEX_PATTERN = StringUtils.join(REGEX_PREFIX, OPERATION_EXPRESSION, REGEX_NESTED_SUFFIX);
    public static final String REFERENCED_REGEX_PATTERN = StringUtils.join("(\\w+\\p{Punct}\\w+?)(", OPERATION_EXPRESSION, REGEX_NESTED_SUFFIX);
    private static final Map<String, SearchOperation> SEARCH_KEY_OPERATION_MAP;

    static {
        SEARCH_KEY_OPERATION_MAP = Map.of(COLON, EQUALITY,
                CLOSING_EXCLAMATION, NEGATION,
                GREATER_THAN_SIGN, GREATER_THAN,
                LESS_THAN_SIGN, LESS_THAN,
                TILDE, LIKE,
                DASH, COLLECTION_CONTAINS);
    }

    public static SearchOperation getSimpleOperation(String input) {
        return SEARCH_KEY_OPERATION_MAP.get(input);
    }

}