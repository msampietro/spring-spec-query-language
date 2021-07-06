package com.msampietro.springspecquerylanguage.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SearchSection {

    private final String key;
    private final String operation;
    private final Object value;
    private final String prefix;
    private final String suffix;
    private final String andOrOperation;

}
