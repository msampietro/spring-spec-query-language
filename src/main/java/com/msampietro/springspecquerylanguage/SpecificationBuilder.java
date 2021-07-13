package com.msampietro.springspecquerylanguage;

import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface SpecificationBuilder<T> {

    /**
     * Builds Specification object for entity T when search string is valid.
     * @param search  String
     * @return Optional<Specification<T>>
     */
    Optional<Specification<T>> parseAndBuild(String search);

}
