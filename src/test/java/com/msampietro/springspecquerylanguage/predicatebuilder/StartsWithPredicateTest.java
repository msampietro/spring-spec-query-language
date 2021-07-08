package com.msampietro.springspecquerylanguage.predicatebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msampietro.springspecquerylanguage.entity.SearchCriteria;
import com.msampietro.springspecquerylanguage.entity.SearchOperation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.criteria.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class StartsWithPredicateTest {

    @Mock
    private Root<Object> root;

    @Mock
    private Join<Object, Object> joinPath;

    @Mock
    private CriteriaQuery<?> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    private final StartsWithPredicate<Object> startsWithPredicate = new StartsWithPredicate<>(new ObjectMapper(), Long.class);

    @Test
    void testToPredicateWhenSingleSearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "name", SearchOperation.STARTS_WITH, "text");
        startsWithPredicate.setSearchCriteria(searchCriteria);

        Expression<String> stringExpression = mock(Expression.class);

        when(root.get(searchCriteria.getKey())).thenReturn(joinPath);
        doReturn(String.class).when(joinPath).getJavaType();
        when(criteriaBuilder.upper(any())).thenReturn(stringExpression);

        startsWithPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).like(stringExpression, "TEXT%");
    }

    @Test
    void testToPredicateWhenNestedSearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "nested", SearchOperation.STARTS_WITH, "name=text");
        startsWithPredicate.setSearchCriteria(searchCriteria);

        Expression<String> stringExpression = mock(Expression.class);

        when(root.join(searchCriteria.getKey())).thenReturn(joinPath);
        when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(joinPath);
        doReturn(String.class).when(joinPath).getJavaType();
        when(criteriaBuilder.upper(any())).thenReturn(stringExpression);

        startsWithPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).like(stringExpression, "TEXT%");
    }

    @Test
    void testToPredicateWhenOneToManySearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "list.nested", SearchOperation.ENDS_WITH, "text");
        startsWithPredicate.setSearchCriteria(searchCriteria);

        Expression<String> stringExpression = mock(Expression.class);

        when(root.join("list")).thenReturn(joinPath);
        when(joinPath.get("nested")).thenReturn(joinPath);
        doReturn(String.class).when(joinPath).getJavaType();
        when(criteriaBuilder.upper(any())).thenReturn(stringExpression);

        startsWithPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).like(stringExpression, "TEXT%");
    }

    @Test
    void testToPredicateWhenNestedOneToManySearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "list.nested", SearchOperation.STARTS_WITH, "name=text");
        startsWithPredicate.setSearchCriteria(searchCriteria);

        Expression<String> stringExpression = mock(Expression.class);

        when(root.join("list")).thenReturn(joinPath);
        when(joinPath.join("nested")).thenReturn(joinPath);
        when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(joinPath);
        doReturn(String.class).when(joinPath).getJavaType();
        when(criteriaBuilder.upper(any())).thenReturn(stringExpression);

        startsWithPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).like(stringExpression, "TEXT%");
    }

}
