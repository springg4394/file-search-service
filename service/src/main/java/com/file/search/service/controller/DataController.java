package com.file.search.service.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.search.service.dto.data.DataDeleteRequestDTO;
import com.file.search.service.dto.data.DataFileIdRequestDTO;
import com.file.search.service.dto.data.DataListRequestDTO;
import com.file.search.service.dto.data.DataListResponseDTO;
import com.file.search.service.model.FileMetadata;
import com.file.search.service.repository.FileMetadataRepository;
import com.file.search.service.service.DatasetService;
import com.file.search.service.service.SearchClassService;
import com.file.search.service.service.SearchTextService;
import com.file.search.service.service.common.MinioUtil;
import com.file.search.service.service.common.SecurityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class DataController {

    private final FileMetadataRepository fileMetadataRepository;
    private final DatasetService         datasetService;
    private final SearchClassService     searchClassService;
    private final SearchTextService      searchTextService;
    private final SecurityService        securityService;
    private final MinioUtil              minioUtil;

    @Value("${minio.bucket-name}")
    private String BUCKET_NAME;

    @PostMapping("/getList")
    public List<DataListResponseDTO> getDataList(@RequestBody DataListRequestDTO dataListRequestDTO) {
        List<FileMetadata> fileMetadataList = new ArrayList<>();

        // 1. workspaceId, directoryId 주어짐
        if (dataListRequestDTO.getWorkspaceId() != null && dataListRequestDTO.getDirectoryId() != null) {
            fileMetadataList = fileMetadataRepository.findByWorkspaceIdAndDirectoryId(
                    dataListRequestDTO.getWorkspaceId(),
                    dataListRequestDTO.getDirectoryId()
            );
        }
        // 2. workspaceId만 주어짐
        else if (dataListRequestDTO.getWorkspaceId() != null) {
            fileMetadataList = fileMetadataRepository.findByWorkspaceId(dataListRequestDTO.getWorkspaceId());
        }
        // 3. directoryId 주어짐
        else if (dataListRequestDTO.getDirectoryId() != null) {
            fileMetadataList = fileMetadataRepository.findByDirectoryId(dataListRequestDTO.getDirectoryId());
        }

        return fileMetadataList.stream()
                .map(fileMetadata -> {
                    DataListResponseDTO dto = new DataListResponseDTO();
                    dto.setId(fileMetadata.getId());
                    dto.setBucketName(fileMetadata.getBucketName());
                    dto.setFileName(fileMetadata.getFileName());
                    dto.setFileSize(fileMetadata.getFileSize());
                    dto.setContentType(fileMetadata.getContentType());
                    dto.setWorkspaceId(fileMetadata.getWorkspaceId());
                    dto.setDirectoryId(fileMetadata.getDirectoryId());
                    dto.setRegisterid(fileMetadata.getRegisterid());
                    dto.setModifyDatetime(fileMetadata.getModifyDatetime());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/getByFileId")
    public ResponseEntity<DataListResponseDTO> getByFileId(@RequestBody DataFileIdRequestDTO dataFileIdRequestDTO) {
        String fileId = dataFileIdRequestDTO.getFileId().trim().replace("\"", ""); 
        return fileMetadataRepository.findById(fileId)
                .map(fileMetadata -> {
                    DataListResponseDTO dto = new DataListResponseDTO();
                    dto.setId(fileMetadata.getId());
                    dto.setBucketName(fileMetadata.getBucketName());
                    dto.setFileName(fileMetadata.getFileName());
                    dto.setFileSize(fileMetadata.getFileSize());
                    dto.setContentType(fileMetadata.getContentType());
                    dto.setWorkspaceId(fileMetadata.getWorkspaceId());
                    dto.setDirectoryId(fileMetadata.getDirectoryId());
                    dto.setRegisterid(fileMetadata.getRegisterid());
                    dto.setModifyDatetime(fileMetadata.getModifyDatetime());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/getRecentList")
    public List<DataListResponseDTO> getRecentDataList() {
        List<FileMetadata> fileMetadataList = new ArrayList<>();

        String loginId = securityService.getLoginId();
        fileMetadataList = fileMetadataRepository.findByRegisterid(loginId);

        return fileMetadataList.stream()
                .map(fileMetadata -> {
                    DataListResponseDTO dto = new DataListResponseDTO();
                    dto.setId(fileMetadata.getId());
                    dto.setBucketName(fileMetadata.getBucketName());
                    dto.setFileName(fileMetadata.getFileName());
                    dto.setFileSize(fileMetadata.getFileSize());
                    dto.setContentType(fileMetadata.getContentType());
                    dto.setWorkspaceId(fileMetadata.getWorkspaceId());
                    dto.setDirectoryId(fileMetadata.getDirectoryId());
                    dto.setRegisterid(fileMetadata.getRegisterid());
                    dto.setModifyDatetime(fileMetadata.getModifyDatetime());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteData(@RequestBody DataDeleteRequestDTO dataDeleteRequestDTO) {
        // 0. MongoDB 삭제
        if (!deleteFromMongoDB(dataDeleteRequestDTO.getFileId())) {
            return false;
        }

        // 1. MinIO 삭제
        if (!deleteFromMinIO(dataDeleteRequestDTO.getObjectName())) {
            return false;
        }

        // 2. Elasticsearch 삭제
        if (!deleteFromElasticsearch(dataDeleteRequestDTO.getFileId())) {
            return false;
        }

        // 3. dataset file 삭제
        if (!deleteFromDataset(dataDeleteRequestDTO.getFileId())) {
            return false;
        }

        return true;
    }

    private boolean deleteFromMongoDB(String fileId) {
        Optional<FileMetadata> fileMetadata = fileMetadataRepository.findById(fileId);
        if (fileMetadata.isPresent()) {
            fileMetadataRepository.deleteById(fileId);
            return true;
        }
        return false;
    }

    private boolean deleteFromMinIO(String objectName) {
        return minioUtil.deleteFile(BUCKET_NAME, objectName);
    }

    private boolean deleteFromElasticsearch(String fileId) {
        boolean classDeleted = searchClassService.deleteData(fileId);
        if (!classDeleted)
            return false;
        return searchTextService.deleteData(fileId);
    }

    private boolean deleteFromDataset(String fileId) {
        return datasetService.deleteDatasetByFileId(fileId);
    }

}
