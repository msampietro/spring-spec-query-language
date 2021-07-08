package com.msampietro.springspecquerylanguage.misc;

import com.msampietro.springspecquerylanguage.entity.SearchOperation;
import com.msampietro.springspecquerylanguage.entity.SearchSection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.msampietro.springspecquerylanguage.entity.SearchOperation.*;
import static com.msampietro.springspecquerylanguage.misc.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class SpecificationUtilsTest {

    @Test
    void testDetermineSplitOperationReturnsEmptyWhenInvalidOperator() {
        Optional<String> result = SpecificationUtils.determineSplitOperation("name:*test*|id:1");
        assertThat(result).isNotPresent();
    }

    @Test
    void testDetermineSplitOperationReturnsComa() {
        Optional<String> result = SpecificationUtils.determineSplitOperation("name:*test*,id:1");
        assertThat(result).contains(COMA);
    }

    @Test
    void testDetermineSplitOperationReturnsOrPredicateFlag() {
        Optional<String> result = SpecificationUtils.determineSplitOperation("name:*test*'id:1");
        assertThat(result).contains(OR_PREDICATE_FLAG);
    }

    @Test
    void testSplitSearchOperationsReturnsSingleSearchWhenNullOperator() {
        String[] result = SpecificationUtils.splitSearchOperations("name:*test*", null);
        assertThat(result).hasSize(1);
    }

    @Test
    void testSplitSearchOperationsReturnsSingleSearchWhenOperatorButNotConcatenatedSearches() {
        String[] result = SpecificationUtils.splitSearchOperations("name:*test*", COMA);
        assertThat(result).hasSize(1);
    }

    @Test
    void testSplitSearchOperationsReturnsMultipleSearchesWhenOperatorAndConcatenatedSearches() {
        String[] result = SpecificationUtils.splitSearchOperations("name:*test*,id:1", COMA);
        assertThat(result).hasSize(2);
    }

    @Test
    void testResolveSearchOperationReturnsContainsWhenColonAndStartEndAsterisks() {
        SearchSection searchSection = new SearchSection("key", COLON, null, ASTERISK, ASTERISK, ",");
        SearchOperation result = SpecificationUtils.resolveSearchOperation(searchSection);
        assertThat(result).isEqualTo(CONTAINS);
    }

    @Test
    void testResolveSearchOperationReturnsEqualityWhenColonButNotStartNorEndAsterisks() {
        SearchSection searchSection = new SearchSection("key", COLON, null, "", "", ",");
        SearchOperation result = SpecificationUtils.resolveSearchOperation(searchSection);
        assertThat(result).isEqualTo(EQUALITY);
    }

    @Test
    void testResolveSearchOperationReturnsEndsWithWhenColonAndStartAsterisk() {
        SearchSection searchSection = new SearchSection("key", COLON, null, ASTERISK, "", ",");
        SearchOperation result = SpecificationUtils.resolveSearchOperation(searchSection);
        assertThat(result).isEqualTo(ENDS_WITH);
    }

    @Test
    void testResolveSearchOperationReturnsEndsWithWhenColonAndEndAsterisk() {
        SearchSection searchSection = new SearchSection("key", COLON, null, "", ASTERISK, ",");
        SearchOperation result = SpecificationUtils.resolveSearchOperation(searchSection);
        assertThat(result).isEqualTo(STARTS_WITH);
    }

    @Test
    void testIsKeyCompoundReturnsSuccessfully() {
        assertThat(SpecificationUtils.isKeyCompound("list.nested")).isTrue();
    }

    @Test
    void testGetCompoundKeysReturnsSuccessfully() {
        String[] result = SpecificationUtils.getCompoundKeys("list.nested1");
        assertThat(result).hasSize(2);
        assertThat(result[0]).isEqualTo("list");
        assertThat(result[1]).isEqualTo("nested1");
    }

}
