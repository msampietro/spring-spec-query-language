package com.msampietro.springspecquerylanguage.predicatebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

public class GreaterThanOrEqualPredicate<T> extends BasePredicate<T> {

    public GreaterThanOrEqualPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        super(objectMapper, idClazz);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.greaterThanOrEqualTo(getCriteriaExpressionKey(root), parseValue(getCriteriaObjectValue().toString(), builder));
    }

}
