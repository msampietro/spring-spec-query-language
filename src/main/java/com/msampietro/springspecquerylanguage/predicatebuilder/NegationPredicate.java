package com.msampietro.springspecquerylanguage.predicatebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msampietro.springspecquerylanguage.misc.SpecificationUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

public class NegationPredicate<T> extends BasePredicate<T> {

    public NegationPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        super(objectMapper, idClazz);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (SpecificationUtils.isValueNullKey(getCriteriaObjectValue().toString()))
            return builder.isNotNull(getCriteriaStringExpressionKey(root));
        return builder.notEqual(getCriteriaStringExpressionKey(root), parseValue(getCriteriaObjectValue().toString(), builder));
    }
}
