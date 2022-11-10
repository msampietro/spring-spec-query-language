package com.msampietro.springspecquerylanguage.predicatebuilder;

import com.msampietro.springspecquerylanguage.entity.SearchCriteria;
import com.msampietro.springspecquerylanguage.entity.SearchOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

@Slf4j
public class SpecificationFactory<T> {

    public Specification<T> getSpecification(SearchCriteria searchCriteria, ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        BasePredicate<T> predicate = this.createPredicate(searchCriteria.getOperation(), objectMapper, idClazz);
        predicate.setSearchCriteria(searchCriteria);
        return predicate;
    }

    private BasePredicate<T> createPredicate(SearchOperation searchOperation, ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        switch (searchOperation){
            case EQUALITY:
                return new EqualityPredicate<>(objectMapper, idClazz);
            case NEGATION:
                return new NegationPredicate<>(objectMapper, idClazz);
            case GREATER_THAN_OR_EQUAL:
                return new GreaterThanOrEqualPredicate<>(objectMapper, idClazz);
            case LESS_THAN_OR_EQUAL:
                return new LessThanOrEqualPredicate<>(objectMapper, idClazz);
            case LIKE:
                return new LikePredicate<>(objectMapper, idClazz);
            case STARTS_WITH:
                return new StartsWithPredicate<>(objectMapper, idClazz);
            case ENDS_WITH:
                return new EndsWithPredicate<>(objectMapper, idClazz);
            case CONTAINS:
                return new ContainsPredicate<>(objectMapper, idClazz);
            case COLLECTION_CONTAINS:
                return new CollectionContainsPredicate<>(objectMapper, idClazz);
            default:
                log.warn("Operation {} is not valid, ignoring this opration", searchOperation);
                return new ConjunctionPredicate<>(objectMapper, idClazz);
        }
    }

}
