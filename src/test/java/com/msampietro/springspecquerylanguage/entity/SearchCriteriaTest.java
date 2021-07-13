package com.msampietro.springspecquerylanguage.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
class SearchCriteriaTest {

    @Test
    void testSearchCriteriaDoesNotBuildNestedSearchCriteriaValueWhenSimpleSearch() {
        SearchCriteria searchCriteria = new SearchCriteria(false, "id", SearchOperation.EQUALITY, "1");
        assertThat(searchCriteria.getValue()).isEqualTo("1");
    }

    @Test
    void testSearchCriteriaBuildNestedSearchCriteriaValueWhenNestedSearch() {
        SearchCriteria searchCriteria = new SearchCriteria(false, "", SearchOperation.EQUALITY, "id=1");
        assertThat(((SearchCriteria) searchCriteria.getValue()).getKey()).isEqualTo("id");
        assertThat(((SearchCriteria) searchCriteria.getValue()).getValue()).isEqualTo("1");
        assertThat(((SearchCriteria) searchCriteria.getValue()).getOperation()).isNull();
        assertThat(((SearchCriteria) searchCriteria.getValue()).isOrPredicate()).isFalse();
    }

}
