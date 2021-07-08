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

@ExtendWith(MockitoExtension.class)
class CollectionContainsPredicateTest {

    @Mock
    private Root<Object> root;

    @Mock
    private Join<Object, Object> joinPath;

    @Mock
    private CriteriaQuery<?> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    private final CollectionContainsPredicate<Object> collectionContainsPredicate = new CollectionContainsPredicate<>(new ObjectMapper(), Long.class);

    @Test
    void testToPredicateWhenSingleSearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "collection", SearchOperation.COLLECTION_CONTAINS, 1);
        collectionContainsPredicate.setSearchCriteria(searchCriteria);

        Expression<Integer> integerExpression = mock(Expression.class);

        when(root.join(searchCriteria.getKey())).thenReturn(joinPath);
        doReturn(Integer.class).when(joinPath).getJavaType();
        when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

        collectionContainsPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).equal(joinPath, integerExpression);
    }

    @Test
    void testToPredicateWhenNestedSearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "nested", SearchOperation.COLLECTION_CONTAINS, "collection=1");
        collectionContainsPredicate.setSearchCriteria(searchCriteria);

        Expression<Integer> integerExpression = mock(Expression.class);

        when(root.join(searchCriteria.getKey())).thenReturn(joinPath);
        when(joinPath.join(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(joinPath);
        doReturn(Integer.class).when(joinPath).getJavaType();
        when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

        collectionContainsPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).equal(joinPath, integerExpression);
    }

    @Test
    void testToPredicateWhenOneToManySearchCriteriaValue() {
        SearchCriteria searchCriteria = new SearchCriteria(null, "list.nested", SearchOperation.COLLECTION_CONTAINS, "collection=1");
        collectionContainsPredicate.setSearchCriteria(searchCriteria);

        Expression<Integer> integerExpression = mock(Expression.class);

        when(root.join("list")).thenReturn(joinPath);
        when(joinPath.join("nested")).thenReturn(joinPath);
        when(joinPath.join(((SearchCriteria) searchCriteria.getValue()).getKey())).thenReturn(joinPath);
        doReturn(Integer.class).when(joinPath).getJavaType();
        when(criteriaBuilder.literal(1)).thenReturn(integerExpression);

        collectionContainsPredicate.toPredicate(root, criteriaQuery, criteriaBuilder);

        verify(criteriaBuilder, times(1)).equal(joinPath, integerExpression);
    }

}
