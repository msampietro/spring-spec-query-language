package com.msampietro.springspecquerylanguage.predicatebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

public class LikePredicate<T> extends BasePredicate<T> {

    LikePredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        super(objectMapper, idClazz);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.like(builder.upper(getCriteriaStringExpressionKey(root).as(String.class)), StringUtils.upperCase(getCriteriaObjectValue().toString()));
    }
}
