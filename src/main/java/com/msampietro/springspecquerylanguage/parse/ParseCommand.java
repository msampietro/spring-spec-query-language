package com.msampietro.springspecquerylanguage.parse;

import com.msampietro.springspecquerylanguage.entity.SearchCriteria;

import java.util.List;

public interface ParseCommand {

    List<SearchCriteria> parse(String[] search, String andOrOperation);

}
