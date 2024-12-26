package com.file.search.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.search.service.dto.search.SearchClassRequestDTO;
import com.file.search.service.model.DetectClassData;
import com.file.search.service.service.SearchClassService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchClassController {

    private final SearchClassService searchClassService;

    @PostMapping("/class")
    @Operation(summary = "클래스 검색", description = "클래스 검색하는 API")
    public List<DetectClassData> searchClass(@RequestBody SearchClassRequestDTO searchClassRequestDTO) throws Exception {
        return searchClassService.searchDatas(searchClassRequestDTO);
    }

}
