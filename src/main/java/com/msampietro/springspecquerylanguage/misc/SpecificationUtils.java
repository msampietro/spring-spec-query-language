package com.msampietro.springspecquerylanguage.misc;

import com.msampietro.springspecquerylanguage.entity.SearchOperation;
import com.msampietro.springspecquerylanguage.entity.SearchSection;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static com.msampietro.springspecquerylanguage.entity.SearchOperation.OR_PREDICATE_FLAG;
import static com.msampietro.springspecquerylanguage.misc.Constants.ASTERISK;
import static com.msampietro.springspecquerylanguage.misc.Constants.COMA;

public final class SpecificationUtils {

    public static Optional<String> determineSplitOperation(String search) {
        if (StringUtils.contains(search, COMA))
            return Optional.of(COMA);
        if (StringUtils.contains(search, OR_PREDICATE_FLAG))
            return Optional.of(OR_PREDICATE_FLAG);
        return Optional.empty();
    }

    public static String[] splitSearchOperations(String search, String operator) {
        if (operator == null)
            return new String[]{search};
        return StringUtils.split(search, operator);
    }

    public static boolean isValueNullKey(String search) {
        return StringUtils.equals(search, "null");
    }

    public static SearchOperation resolveSearchOperation(SearchSection searchSection) {
        var parsedSearchOperation = SearchOperation.getSimpleOperation(StringUtils.substring(searchSection.getOperation(), 0, 1));
        if (parsedSearchOperation == SearchOperation.EQUALITY) {
            boolean startWithAsterisk = StringUtils.contains(searchSection.getPrefix(), ASTERISK);
            boolean endWithAsterisk = StringUtils.contains(searchSection.getSuffix(), ASTERISK);
            if (startWithAsterisk && endWithAsterisk)
                return SearchOperation.CONTAINS;
            if(!startWithAsterisk && !endWithAsterisk)
                return SearchOperation.EQUALITY;
            if (startWithAsterisk)
                return SearchOperation.ENDS_WITH;
            return SearchOperation.STARTS_WITH;
        }
        return parsedSearchOperation;
    }

    private SpecificationUtils() {

    }

}
