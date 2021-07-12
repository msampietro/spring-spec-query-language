package com.msampietro.springspecquerylanguage.parse;

import com.msampietro.springspecquerylanguage.entity.SearchCriteria;
import com.msampietro.springspecquerylanguage.entity.SearchSection;
import com.msampietro.springspecquerylanguage.misc.SpecificationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseParseCommand implements ParseCommand {

    private final Pattern pattern;

    BaseParseCommand(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public List<SearchCriteria> parse(String[] search, String andOrOperator) {
        var listOfResults = new ArrayList<SearchCriteria>();
        for (var s : search) {
            var matcher = pattern.matcher(s);
            if (matcher.matches()) {
                var section = process(matcher);
                section.setAndOrOperator(andOrOperator);
                listOfResults.add(buildCriteriaFromSection(section));
            }
        }
        return listOfResults;
    }

    private SearchCriteria buildCriteriaFromSection(SearchSection searchSection) {
        var searchOperation = SpecificationUtils.resolveSearchOperation(searchSection);
        return new SearchCriteria(searchSection.getAndOrOperator(),
                searchSection.getKey(),
                searchOperation,
                searchSection.getValue());
    }

    protected abstract SearchSection process(Matcher matcher);

}
