package com.msampietro.springspecquerylanguage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msampietro.springspecquerylanguage.entity.SearchCriteria;
import com.msampietro.springspecquerylanguage.misc.SpecificationUtils;
import com.msampietro.springspecquerylanguage.parse.*;
import com.msampietro.springspecquerylanguage.predicatebuilder.SpecificationFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.*;

public class SpecificationBuilderImpl<T> implements SpecificationBuilder<T> {

    private final ObjectMapper objectMapper;
    private final Class<? extends Serializable> idClazz;
    private static final List<ParseCommand> PARSE_COMMANDS = new ArrayList<>(Arrays.asList(
            new SimpleParseCommand(),
            new NestedParseCommand(),
            new ReferencedSimpleParseCommand(),
            new ReferencedCompoundParseCommand()
    ));

    public SpecificationBuilderImpl(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        this.objectMapper = objectMapper;
        this.idClazz = idClazz;
    }

    @Override
    public Optional<Specification<T>> parseAndBuild(String search) {
        if (StringUtils.isBlank(search))
            return Optional.empty();
        var params = new ArrayList<SearchCriteria>();
        var andOrOperation = SpecificationUtils.determineSplitOperation(search).orElse(null);
        var searchQueries = SpecificationUtils.splitSearchOperations(search, andOrOperation);
        for (var parseCommand : PARSE_COMMANDS)
            params.addAll(parseCommand.parse(searchQueries, andOrOperation));
        return build(params);
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
