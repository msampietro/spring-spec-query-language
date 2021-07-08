package com.msampietro.springspecquerylanguage.parse;

import com.msampietro.springspecquerylanguage.entity.SearchSection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.msampietro.springspecquerylanguage.entity.SearchOperation.*;

public class SimpleParseCommand extends BaseParseCommand {

    private static final String SIMPLE_REGEX_PATTERN = String.join("", REGEX_PREFIX, OPERATION_EXPRESSION, REGEX_SIMPLE_SUFFIX);

    public SimpleParseCommand() {
        super(Pattern.compile(SIMPLE_REGEX_PATTERN));
    }

    @Override
    protected SearchSection process(Matcher matcher) {
        return new SearchSection(matcher.group(1),
                matcher.group(2),
                matcher.group(4),
                matcher.group(3),
                matcher.group(5));
    }
}
