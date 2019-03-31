package tn.insat.pfe.searchservice.services.fallbacks;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import tn.insat.pfe.searchservice.dtos.FileGetDto;
import tn.insat.pfe.searchservice.dtos.LdaTopicsDescriptionGetDto;
import tn.insat.pfe.searchservice.dtos.SearchDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchServiceSilentFallback {

    public SearchDto silentFind(String query, Pageable pageable) throws IOException {
        Page<FileGetDto> fileGetDtos = new PageImpl<>
                (new ArrayList<FileGetDto>(), PageRequest.of(pageable.getPageNumber(),pageable.getPageSize()),0);
        return new SearchDto(fileGetDtos,new ArrayList<String>(),0,new ArrayList<LdaTopicsDescriptionGetDto>());
    }
//    public Page<FileGetDto> silentFindBy(String by, String value, Pageable pageable) throws IOException {
//        return null;
//    }
//    public FileGetDto silentFindById(String id) throws IOException {
//        return null;
//    }
//    public Page<FileGetDto> silentFindAllSortBy(String sortBy, Pageable pageable) throws IOException {
//        return null;
//    }
//    public List<LdaTopicsDescriptionGetDto> silentGetLdaTopics() throws IOException {
//        return null;
//    }
}
