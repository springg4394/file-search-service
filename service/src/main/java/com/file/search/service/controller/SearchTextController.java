package com.file.search.service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.search.service.dto.search.SearchTextRequestDTO;
import com.file.search.service.service.SearchTextService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchTextController {

    private final SearchTextService searchTextService;

    @PostMapping("/text")
    @Operation(summary = "텍스트 검색", description = "텍스트 검색하는 API")
    public String searchClass(@RequestBody SearchTextRequestDTO searchTextRequestDTO) throws Exception {
        return searchTextService.searchText(searchTextRequestDTO);
    }

}