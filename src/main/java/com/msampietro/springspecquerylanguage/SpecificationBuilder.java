package com.msampietro.springspecquerylanguage;

import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface SpecificationBuilder<T> {

    Optional<Specification<T>> parseAndBuild(String search);

}
