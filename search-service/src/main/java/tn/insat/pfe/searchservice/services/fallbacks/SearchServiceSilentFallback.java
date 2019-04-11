package tn.insat.pfe.searchservice.services.fallbacks;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import tn.insat.pfe.searchservice.dtos.FileGetDto;
import tn.insat.pfe.searchservice.dtos.SearchDto;
import java.util.ArrayList;

public class SearchServiceSilentFallback {

    public SearchDto silentFind(String query, Pageable pageable) {
        Page<FileGetDto> fileGetDtos = new PageImpl<>
                (new ArrayList<>(), PageRequest.of(pageable.getPageNumber(),pageable.getPageSize()),0);
        return new SearchDto(fileGetDtos,new ArrayList<>(),0,new ArrayList<>());
    }

}
