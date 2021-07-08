package com.msampietro.springspecquerylanguage.predicatebuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msampietro.springspecquerylanguage.entity.SearchCriteria;
import com.msampietro.springspecquerylanguage.entity.SearchOperation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.criteria.*;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class NegationPredicateTest {

    @Mock
    private Root<Object> root;

    @Mock
    private Join<Object, Object> joinPath;

    @Mock
    private CriteriaQuery<?> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    private final NegationPredicate<Object> negationPredicate = new NegationPredicate<>(new ObjectMapper(), Long.class);

    @Test
    void testToPredicateWhenSingleSearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "id", SearchOperation.NEGATION, "1");
        negationPredicate.setSearchCriteria(searchCriteria);

        Expression<Integer> integerExpression = mock(Expression.class);

        when(root.get(searchCriteria.getKey())).thenReturn(joinPath);
        doReturn(Integer.class).when(joinPath).getJavaType();
        when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

        negationPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).notEqual(joinPath, integerExpression);
    }

    @Test
    void testToPredicateWhenSingleSearchCriteriaValueNullableSearch() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "id", SearchOperation.NEGATION, "null");
        negationPredicate.setSearchCriteria(searchCriteria);

        when(root.get(searchCriteria.getKey())).thenReturn(joinPath);
        doReturn(Integer.class).when(joinPath).getJavaType();

        negationPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).isNotNull(joinPath);
    }

    @Test
    void testToPredicateWhenNestedSearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "nested", SearchOperation.NEGATION, "id=1");
        negationPredicate.setSearchCriteria(searchCriteria);

        Expression<Integer> integerExpression = mock(Expression.class);

        when(root.join(searchCriteria.getKey())).thenReturn(joinPath);
        when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(joinPath);
        doReturn(Integer.class).when(joinPath).getJavaType();
        when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

        negationPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).notEqual(joinPath, integerExpression);
    }

    @Test
    void testToPredicateWhenOneToManySearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "list.nested", SearchOperation.NEGATION, "1");
        negationPredicate.setSearchCriteria(searchCriteria);

        Expression<Integer> integerExpression = mock(Expression.class);

        when(root.join("list")).thenReturn(joinPath);
        when(joinPath.get("nested")).thenReturn(joinPath);
        doReturn(Integer.class).when(joinPath).getJavaType();
        when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

        negationPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).notEqual(joinPath, integerExpression);
    }

    @Test
    void testToPredicateWhenNestedOneToManySearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "list.nested", SearchOperation.NEGATION, "id=1");
        negationPredicate.setSearchCriteria(searchCriteria);

        Expression<Integer> integerExpression = mock(Expression.class);

        when(root.join("list")).thenReturn(joinPath);
        when(joinPath.join("nested")).thenReturn(joinPath);
        when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(joinPath);
        doReturn(Integer.class).when(joinPath).getJavaType();
        when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

        negationPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).notEqual(joinPath, integerExpression);
    }

}
