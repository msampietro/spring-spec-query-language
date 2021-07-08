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
class GreaterThanOrEqualPredicateTest {

    @Mock
    private Root<Object> root;

    @Mock
    private Path path;

    @Mock
    private Join<Object, Object> joinPath;

    @Mock
    private CriteriaQuery<?> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    private final GreaterThanOrEqualPredicate<Object> greaterThanOrEqualPredicate = new GreaterThanOrEqualPredicate<>(new ObjectMapper(), Long.class);

    @Test
    void testToPredicateWhenSingleSearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "id", SearchOperation.GREATER_THAN_OR_EQUAL, "1");
        greaterThanOrEqualPredicate.setSearchCriteria(searchCriteria);

        Expression<Integer> integerExpression = mock(Expression.class);

        when(root.get(searchCriteria.getKey())).thenReturn(path);
        doReturn(Integer.class).when(path).getJavaType();
        when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

        greaterThanOrEqualPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).greaterThanOrEqualTo(path, integerExpression);
    }

    @Test
    void testToPredicateWhenNestedSearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "nested", SearchOperation.GREATER_THAN_OR_EQUAL, "id=1");
        greaterThanOrEqualPredicate.setSearchCriteria(searchCriteria);

        Expression<Integer> integerExpression = mock(Expression.class);

        when(root.join(searchCriteria.getKey())).thenReturn(joinPath);
        when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(path);
        doReturn(Integer.class).when(path).getJavaType();
        when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

        greaterThanOrEqualPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).greaterThanOrEqualTo(path, integerExpression);
    }

    @Test
    void testToPredicateWhenOneToManySearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "list.nested", SearchOperation.GREATER_THAN_OR_EQUAL, "1");
        greaterThanOrEqualPredicate.setSearchCriteria(searchCriteria);

        Expression<Integer> integerExpression = mock(Expression.class);

        when(root.join("list")).thenReturn(joinPath);
        when(joinPath.get("nested")).thenReturn(path);
        doReturn(Integer.class).when(path).getJavaType();
        when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

        greaterThanOrEqualPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).greaterThanOrEqualTo(path, integerExpression);
    }

    @Test
    void testToPredicateWhenNestedOneToManySearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "list.nested", SearchOperation.GREATER_THAN_OR_EQUAL, "id=1");
        greaterThanOrEqualPredicate.setSearchCriteria(searchCriteria);

        Expression<Integer> integerExpression = mock(Expression.class);

        when(root.join("list")).thenReturn(joinPath);
        when(joinPath.join("nested")).thenReturn(joinPath);
        when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(path);
        doReturn(Integer.class).when(path).getJavaType();
        when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

        greaterThanOrEqualPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).greaterThanOrEqualTo(path, integerExpression);
    }

}
