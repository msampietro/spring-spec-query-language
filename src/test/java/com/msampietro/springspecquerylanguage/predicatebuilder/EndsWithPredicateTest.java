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

@ExtendWith(MockitoExtension.class)
class EndsWithPredicateTest {

    @Mock
    private Root<Object> root;

    @Mock
    private Join<Object, Object> joinPath;

    @Mock
    private CriteriaQuery<?> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    private final EndsWithPredicate<Object> endsWithPredicate = new EndsWithPredicate<>(new ObjectMapper(), Long.class);

    @Test
    void testToPredicateWhenSingleSearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "name", SearchOperation.ENDS_WITH, "text");
        endsWithPredicate.setSearchCriteria(searchCriteria);

        Expression<String> stringExpression = mock(Expression.class);

        when(root.get(searchCriteria.getKey())).thenReturn(joinPath);
        doReturn(String.class).when(joinPath).getJavaType();
        when(criteriaBuilder.upper(any())).thenReturn(stringExpression);

        endsWithPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).like(stringExpression, "%TEXT");
    }

    @Test
    void testToPredicateWhenNestedSearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "nested", SearchOperation.ENDS_WITH, "name=text");
        endsWithPredicate.setSearchCriteria(searchCriteria);

        Expression<String> stringExpression = mock(Expression.class);

        when(root.join(searchCriteria.getKey())).thenReturn(joinPath);
        when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(joinPath);
        doReturn(String.class).when(joinPath).getJavaType();
        when(criteriaBuilder.upper(any())).thenReturn(stringExpression);

        endsWithPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).like(stringExpression, "%TEXT");
    }

    @Test
    void testToPredicateWhenOneToManySearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "list.nested", SearchOperation.ENDS_WITH, "text");
        endsWithPredicate.setSearchCriteria(searchCriteria);

        Expression<String> stringExpression = mock(Expression.class);

        when(root.join("list")).thenReturn(joinPath);
        when(joinPath.get("nested")).thenReturn(joinPath);
        doReturn(String.class).when(joinPath).getJavaType();
        when(criteriaBuilder.upper(any())).thenReturn(stringExpression);

        endsWithPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).like(stringExpression, "%TEXT");
    }

    @Test
    void testToPredicateWhenNestedOneToManySearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "list.nested", SearchOperation.ENDS_WITH, "name=text");
        endsWithPredicate.setSearchCriteria(searchCriteria);

        Expression<String> stringExpression = mock(Expression.class);

        when(root.join("list")).thenReturn(joinPath);
        when(joinPath.join("nested")).thenReturn(joinPath);
        when(joinPath.get(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(joinPath);
        doReturn(String.class).when(joinPath).getJavaType();
        when(criteriaBuilder.upper(any())).thenReturn(stringExpression);

        endsWithPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).like(stringExpression, "%TEXT");
    }

}
