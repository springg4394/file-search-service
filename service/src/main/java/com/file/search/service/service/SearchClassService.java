package com.file.search.service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.file.search.service.dto.search.SearchClassRequestDTO;
import com.file.search.service.model.DetectClassData;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchClassService {

    private final ElasticsearchClient elasticsearchClient;
    private final String indexName = "image-model1";

    public List<DetectClassData> searchDatas(SearchClassRequestDTO searchClassRequestDTO) throws Exception {
        log.info("Searching for className: {} with confidence >= {}", searchClassRequestDTO.getClassName());
 
        SearchRequest searchRequest = SearchRequest.of(s -> s
            .index(indexName)
            .query(q -> q
                .term(m -> m
                    .field("predictions.class_name.keyword")
                    .value(searchClassRequestDTO.getClassName())
                )
            )
        );

        try {
            SearchResponse<DetectClassData> response = elasticsearchClient.search(searchRequest, DetectClassData.class);
    
            log.info("Search returned {} hits.", response.hits().total().value());

            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error executing search query", e);
            throw new Exception("Error executing search query", e);
        }
    }

    public boolean deleteData(String fileId) {
        try {
            SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(indexName)
                .query(q -> q
                    .term(m -> m
                        .field("file_id")
                        .value(fileId)
                    )
                )
            );

            SearchResponse<Object> searchResponse = elasticsearchClient.search(searchRequest, Object.class);
            if (searchResponse.hits().total().value() > 0) {
                String documentId = searchResponse.hits().hits().get(0).id();

                DeleteRequest deleteRequest = DeleteRequest.of(d -> d
                    .index(indexName)
                    .id(documentId) 
                );

                elasticsearchClient.delete(deleteRequest);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("Error executing search query", e);
            return false;
        }
    }


}
