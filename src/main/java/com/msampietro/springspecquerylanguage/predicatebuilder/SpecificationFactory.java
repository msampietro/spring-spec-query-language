package com.msampietro.springspecquerylanguage.predicatebuilder;

import com.msampietro.springspecquerylanguage.entity.SearchCriteria;
import com.msampietro.springspecquerylanguage.entity.SearchOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

public class SpecificationFactory<T> {

    private final Map<SearchOperation, BasePredicate<T>> operationMap = new EnumMap<>(SearchOperation.class);

    public SpecificationFactory(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        operationMap.put(SearchOperation.EQUALITY, new EqualityPredicate<>(objectMapper, idClazz));
        operationMap.put(SearchOperation.NEGATION, new NegationPredicate<>(objectMapper, idClazz));
        operationMap.put(SearchOperation.GREATER_THAN, new GreaterThanPredicate<>(objectMapper, idClazz));
        operationMap.put(SearchOperation.LESS_THAN, new LessThanPredicate<>(objectMapper, idClazz));
        operationMap.put(SearchOperation.LIKE, new LikePredicate<>(objectMapper, idClazz));
        operationMap.put(SearchOperation.STARTS_WITH, new StartsWithPredicate<>(objectMapper, idClazz));
        operationMap.put(SearchOperation.ENDS_WITH, new EndsWithPredicate<>(objectMapper, idClazz));
        operationMap.put(SearchOperation.CONTAINS, new ContainsPredicate<>(objectMapper, idClazz));
        operationMap.put(SearchOperation.COLLECTION_CONTAINS, new CollectionContainsPredicate<>(objectMapper, idClazz));
    }

    public Specification<T> getSpecification(SearchCriteria searchCriteria) {
        BasePredicate<T> predicate = operationMap.get(searchCriteria.getOperation());
        predicate.setSearchCriteria(searchCriteria);
        return predicate;
    }

}
