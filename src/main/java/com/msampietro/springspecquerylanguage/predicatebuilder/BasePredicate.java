package com.msampietro.springspecquerylanguage.predicatebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msampietro.springspecquerylanguage.entity.SearchCriteria;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.io.Serializable;

import static com.msampietro.springspecquerylanguage.misc.SpecificationUtils.getCompoundKeys;
import static com.msampietro.springspecquerylanguage.misc.SpecificationUtils.isKeyCompound;

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
        return searchCriteria.isValueSearchCriteria() ? ((SearchCriteria) searchCriteria.getValue()).getValue() : searchCriteria.getValue();
    }

    protected Path<Comparable<? super Object>> getCriteriaExpressionKey(Root<T> root) {
        Path<Comparable<? super Object>> keyPath = rootGetPath(root);
        javaType = keyPath.getJavaType();
        return keyPath;
    }

    private <X> Path<X> rootGetPath(Root<T> root) {
        if (isKeyCompound(searchCriteria.getKey())) {
            String[] keys = getCompoundKeys(searchCriteria.getKey());
            return getCompoundCriteriaExpressionKey(root, keys);
        }
        return searchCriteria.isValueSearchCriteria() ?
                root.join(searchCriteria.getKey()).get(((SearchCriteria) searchCriteria.getValue()).getKey()) :
                root.get(searchCriteria.getKey());
    }

    protected <X> Path<X> getCompoundCriteriaExpressionKey(Root<T> root, String[] keys) {
        return searchCriteria.isValueSearchCriteria() ?
                root.join(keys[0]).join(keys[1]).get(((SearchCriteria) searchCriteria.getValue()).getKey()) :
                root.join(keys[0]).get(keys[1]);
    }

    protected Path<Comparable<? super Object>> getCriteriaExpressionJoinKey(Root<T> root) {
        Path<Comparable<? super Object>> keyPath = rootJoinPath(root);
        javaType = keyPath.getJavaType();
        return keyPath;
    }

    private <X> Path<X> rootJoinPath(Root<T> root) {
        if (isKeyCompound(searchCriteria.getKey())) {
            String[] keys = getCompoundKeys(searchCriteria.getKey());
            return root.join(keys[0]).join(keys[1]).join(((SearchCriteria) searchCriteria.getValue()).getKey());
        }
        return searchCriteria.isValueSearchCriteria() ?
                root.join(searchCriteria.getKey()).join(((SearchCriteria) searchCriteria.getValue()).getKey()) :
                root.join(searchCriteria.getKey());
    }

    protected Path<String> getCriteriaStringExpressionKey(Root<T> root) {
        Path<String> keyPath = rootGetPath(root);
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
