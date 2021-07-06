package com.msampietro.springspecquerylanguage.predicatebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msampietro.springspecquerylanguage.entity.SearchCriteria;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.io.Serializable;

import static com.msampietro.springspecquerylanguage.misc.Constants.DOT;

@Log4j2
public abstract class BasePredicate<T> implements Specification<T> {

    private static final String PARSE_ERROR = "Criteria Value cannot be parsed: {}";
    private transient SearchCriteria searchCriteria;
    private final ObjectMapper objectMapper;
    private Class<?> javaType;
    private final Class<? extends Serializable> idClazz;

    BasePredicate(ObjectMapper objectMapper, Class<? extends Serializable> idClazz) {
        this.objectMapper = objectMapper;
        this.idClazz = idClazz;
    }

    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    protected Object getCriteriaObjectValue() {
        Object valueExpression = searchCriteria.getValue();
        if (searchCriteria.getValue() instanceof SearchCriteria)
            valueExpression = ((SearchCriteria) searchCriteria.getValue()).getValue();
        return valueExpression;
    }

    protected Path<Comparable<? super Object>> getCriteriaExpressionKey(Root<T> root) {
        Path<Comparable<? super Object>> keyPath = rootGetPath(root);
        javaType = keyPath.getJavaType();
        return keyPath;
    }

    private Path<Comparable<? super Object>> rootGetPath(Root<T> root) {
        return searchCriteria.getValue() instanceof SearchCriteria ?
                root.join(searchCriteria.getKey()).get(((SearchCriteria) searchCriteria.getValue()).getKey()) :
                root.get(searchCriteria.getKey());
    }

    protected Path<Comparable<? super Object>> getCriteriaExpressionJoinKey(Root<T> root) {
        Path<Comparable<? super Object>> keyPath = rootJoinPath(root);
        javaType = keyPath.getJavaType();
        return keyPath;
    }

    private Path<Comparable<? super Object>> rootJoinPath(Root<T> root) {
        return searchCriteria.getValue() instanceof SearchCriteria ?
                root.join(searchCriteria.getKey()).join(((SearchCriteria) searchCriteria.getValue()).getKey()) :
                root.join(searchCriteria.getKey());
    }

    protected Path<String> getCriteriaStringExpressionKey(Root<T> root) {
        if (StringUtils.contains(searchCriteria.getKey(), DOT)) {
            String[] keys = StringUtils.split(searchCriteria.getKey(), DOT);
            return getCompoundCriteriaStringExpressionKey(root, keys);
        }
        Path<String> keyPath = searchCriteria.getValue() instanceof SearchCriteria ?
                root.join(searchCriteria.getKey()).get(((SearchCriteria) searchCriteria.getValue()).getKey()) :
                root.get(searchCriteria.getKey());
        javaType = keyPath.getJavaType();
        return keyPath;
    }

    protected Path<String> getCompoundCriteriaStringExpressionKey(Root<T> root, String[] keys) {
        Path<String> keyPath = searchCriteria.getValue() instanceof SearchCriteria ?
                root.join(keys[0]).join(keys[1]).get(((SearchCriteria) searchCriteria.getValue()).getKey()) :
                root.join(keys[0]).get(keys[1]);
        javaType = keyPath.getJavaType();
        return keyPath;
    }


    protected Expression<Comparable<? super Object>> parseValue(String value, CriteriaBuilder builder) {
        if (javaType == Serializable.class)
            return builder.literal(parseJavaType(value, idClazz));
        return builder.literal(parseJavaType(value, javaType));
    }

    @SuppressWarnings("unchecked")
    private Comparable<? super Object> parseJavaType(String value, Class<?> clazz) {
        Comparable<? super Object> parsedValue = null;
        try {
            parsedValue = (Comparable<? super Object>) objectMapper.readValue(objectMapper.writeValueAsBytes(value), clazz);
        } catch (Exception ex) {
            log.debug(PARSE_ERROR, ex.getMessage());
        }
        return parsedValue;
    }

}
