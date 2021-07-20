package com.msampietro.springspecquerylanguage.predicatebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

import static com.msampietro.springspecquerylanguage.misc.Constants.PERCENT_SIGN;

public class StartsWithPredicate<T> extends BasePredicate<T> {

    public StartsWithPredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        super(objectMapper, idClazz);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.like(builder.upper(getCriteriaStringExpressionKey(root).as(String.class)), StringUtils.join(StringUtils.upperCase(getCriteriaObjectValue().toString()), PERCENT_SIGN));
    }
}
