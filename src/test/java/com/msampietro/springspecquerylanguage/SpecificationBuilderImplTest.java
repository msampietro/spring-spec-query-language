package com.msampietro.springspecquerylanguage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msampietro.springspecquerylanguage.predicatebuilder.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class SpecificationBuilderImplTest {

    private final SpecificationBuilderImpl<Object> specificationBuilder = new SpecificationBuilderImpl(new ObjectMapper(), Long.class);

    @ParameterizedTest
    @ValueSource(strings = {"", "asdasdasda", " ", "name~textNameid_1"})
    void testParseAndBuildReturnsEmptyWhenInvalidSearch(String search) {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
        assertThat(result).isNotPresent();
    }

    @ParameterizedTest
    @NullSource
    void testParseAndBuildReturnsEmptyWhenNullSearch(String search) {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
        assertThat(result).isNotPresent();
    }

    @ParameterizedTest
    @ValueSource(strings = {"id:1", "nested:{id=1}", "list.id:1", "list.nested:{id=1}"})
    void testParseAndBuildReturnsValidEqualitySpecificationWhenValidSearch(String search) {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
        assertThat(result).isPresent();
        assertThat(result.get()).isInstanceOf(EqualityPredicate.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"collection-textValue", "nested-{collection=textValue}", "list.nested-{collection=textValue}"})
    void testParseAndBuildReturnsValidCollectionContainsSpecificationWhenValidSearch(String search) {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
        assertThat(result).isPresent();
        assertThat(result.get()).isInstanceOf(CollectionContainsPredicate.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"name:*search*", "nested:{name=*search*}", "list.name:*search*", "list.nested:{name=*search*}"})
    void testParseAndBuildReturnsValidContainsSpecificationWhenValidSearch(String search) {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
        assertThat(result).isPresent();
        assertThat(result.get()).isInstanceOf(ContainsPredicate.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"name:*search", "nested:{name=*search}", "list.name:*search", "list.nested:{name=*search}"})
    void testParseAndBuildReturnsValidEndsWithSpecificationWhenValidSearch(String search) {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
        assertThat(result).isPresent();
        assertThat(result.get()).isInstanceOf(EndsWithPredicate.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"name:search*", "nested:{name=search*}", "list.name:search*", "list.nested:{name=search*}"})
    void testParseAndBuildReturnsValidStartsWithSpecificationWhenValidSearch(String search) {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
        assertThat(result).isPresent();
        assertThat(result.get()).isInstanceOf(StartsWithPredicate.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id>1", "nested>{id=1}", "list.id>1", "list.nested>{id=1}"})
    void testParseAndBuildReturnsValidGreaterThanSpecificationWhenValidNumericalSearch(String search) {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
        assertThat(result).isPresent();
        assertThat(result.get()).isInstanceOf(GreaterThanOrEqualPredicate.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id<1", "nested<{id=1}", "list.id<1", "list.nested<{id=1}"})
    void testParseAndBuildReturnsValidLessThanSpecificationWhenValidNumericalSearch(String search) {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
        assertThat(result).isPresent();
        assertThat(result.get()).isInstanceOf(LessThanOrEqualPredicate.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"name~textName", "nested~{name=textName}", "list.name~textName", "list.nested~{name=textName}"})
    void testParseAndBuildReturnsValidLikeSpecificationWhenValidStringSearch(String search) {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
        assertThat(result).isPresent();
        assertThat(result.get()).isInstanceOf(LikePredicate.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"name!notThisName", "nested!{name=notThisName}", "list.name!notThisName", "list.nested!{name=notThisName}"})
    void testParseAndBuildReturnsValidNegationSpecificationWhenValidSearch(String search) {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild(search);
        assertThat(result).isPresent();
        assertThat(result.get()).isInstanceOf(NegationPredicate.class);
    }

    @Test
    void testParseAndBuildReturnsCombinedSpecificationWhenMultipleANDSearches() {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild("name~textName,id<1");
        assertThat(result).isPresent();
    }

    @Test
    void testParseAndBuildReturnsCombinedSpecificationWhenMultipleORSearches() {
        Optional<Specification<Object>> result = specificationBuilder.parseAndBuild("name~textName'id<1");
        assertThat(result).isPresent();
    }

}
