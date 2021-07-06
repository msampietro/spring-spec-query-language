package com.msampietro.springspecquerylanguage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msampietro.springspecquerylanguage.entity.SearchCriteria;
import com.msampietro.springspecquerylanguage.entity.SearchOperation;
import com.msampietro.springspecquerylanguage.entity.SearchSection;
import com.msampietro.springspecquerylanguage.misc.SpecificationUtils;
import com.msampietro.springspecquerylanguage.predicatebuilder.SpecificationFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecificationBuilderImpl<T> implements SpecificationBuilder<T> {

    private final ObjectMapper objectMapper;
    private final Class<? extends Serializable> idClazz;
    private final Pattern simpleSearchPattern;
    private final Pattern nestedSearchPattern;
    private final Pattern referencedSearchPattern;

    public SpecificationBuilderImpl(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        this.objectMapper = objectMapper;
        this.idClazz = idClazz;
        this.simpleSearchPattern = Pattern.compile(SearchOperation.SIMPLE_REGEX_PATTERN);
        this.nestedSearchPattern = Pattern.compile(SearchOperation.NESTED_REGEX_PATTERN);
        this.referencedSearchPattern = Pattern.compile(SearchOperation.REFERENCED_REGEX_PATTERN);
    }

    @Override
    public Optional<Specification<T>> parseAndBuild(String search) {
        if (StringUtils.isBlank(search))
            return Optional.empty();
        var params = new ArrayList<SearchCriteria>();
        var andOrOperation = SpecificationUtils.determineSplitOperation(search).orElse(null);
        var searchQueries = SpecificationUtils.splitSearchOperations(search, andOrOperation);
        for (String query : searchQueries) {
            var simpleMatcher = simpleSearchPattern.matcher(query);
            if (simpleMatcher.matches()) {
                processSimpleMatcher(params, simpleMatcher, andOrOperation);
            } else {
                var compoundMatcher = nestedSearchPattern.matcher(query);
                if (compoundMatcher.matches())
                    processCompoundMatcher(params, compoundMatcher, andOrOperation);
                else {
                    var referencedMatcher = referencedSearchPattern.matcher(query);
                    if (referencedMatcher.matches())
                        processReferencedMatcher(params, referencedMatcher, andOrOperation);
                }
            }
        }
        return build(params);
    }

    private void processSimpleMatcher(List<SearchCriteria> params, Matcher simpleMatcher, String andOrOperation) {
        var searchSection = new SearchSection(simpleMatcher.group(1),
                simpleMatcher.group(2),
                simpleMatcher.group(4),
                simpleMatcher.group(3),
                simpleMatcher.group(5),
                andOrOperation);
        addSearchCriteria(params, searchSection);
    }

    private void processCompoundMatcher(List<SearchCriteria> params, Matcher compoundMatcher, String andOrOperation) {
        var value = StringUtils.join(compoundMatcher.group(4), compoundMatcher.group(5), compoundMatcher.group(7));
        var searchSection = new SearchSection(compoundMatcher.group(1),
                compoundMatcher.group(2),
                value,
                compoundMatcher.group(6),
                compoundMatcher.group(8),
                andOrOperation);
        addSearchCriteria(params, searchSection);
    }

    private void processReferencedMatcher(List<SearchCriteria> params, Matcher referencedMatcher, String andOrOperation) {
        var value = StringUtils.join(referencedMatcher.group(4), referencedMatcher.group(5), referencedMatcher.group(7));
        var searchSection = new SearchSection(referencedMatcher.group(1),
                referencedMatcher.group(2),
                value,
                referencedMatcher.group(6),
                referencedMatcher.group(8),
                andOrOperation);
        addSearchCriteria(params, searchSection);
    }

    private void addSearchCriteria(List<SearchCriteria> params, SearchSection searchSection) {
        var searchOperation = SpecificationUtils.resolveSearchOperation(searchSection);
        params.add(new SearchCriteria(searchSection.getAndOrOperation(),
                searchSection.getKey(),
                searchOperation,
                searchSection.getValue()));
    }

    private Optional<Specification<T>> build(List<SearchCriteria> params) {
        if (params.isEmpty())
            return Optional.empty();
        var specFactory = new SpecificationFactory<T>(objectMapper, idClazz);
        Specification<T> result = specFactory.getSpecification(params.get(0));
        for (var i = 1; i < params.size(); i++)
            result = params.get(i).isOrPredicate()
                    ? Objects.requireNonNull(Specification.where(result)).or(specFactory.getSpecification(params.get(i)))
                    : Objects.requireNonNull(Specification.where(result)).and(specFactory.getSpecification(params.get(i)));
        return Optional.of(result);
    }
}
