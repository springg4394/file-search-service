package com.file.search.service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.file.search.service.dto.upload.FileMetadataDTO;
import com.file.search.service.dto.upload.FileUploadRequestDTO;
import com.file.search.service.dto.upload.KafkaMessageDTO;
import com.file.search.service.model.FileMetadata;
import com.file.search.service.repository.FileMetadataRepository;
import com.file.search.service.service.common.IdGenerateService;
import com.file.search.service.service.common.KafkaProducerService;
import com.file.search.service.service.common.MinioUtil;
import com.file.search.service.service.common.SecurityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final MinioUtil minioUtil;
    private final FileMetadataRepository fileMetadataRepository;
    private final KafkaProducerService   kafkaProducerService;
    private final IdGenerateService      idGenerateService;
    private final SecurityService        securityService;

    @Value("${minio.bucket-name}")
    private String BUCKET_NAME;

    @PostMapping(value = "/file", consumes = "multipart/form-data")
    @Operation(summary = "파일 업로드", description = "파일 업로드하는 API", requestBody = @RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = FileUploadRequestDTO.class))))
    public ResponseEntity<String> uploadFile(
            @RequestPart(value = "file") MultipartFile file, @RequestPart("objectName") String objectName, @RequestPart("workspaceId") String workspaceId,
            @RequestPart(value = "directoryId", required = false) String directoryId, @RequestPart("detect") String detect, @RequestPart("text") String text) {
        if (!file.isEmpty()) {
            // 0. minio 파일 업로드
            String result = minioUtil.uploadFile(file, objectName, BUCKET_NAME);

            if (result != null) {
                // 1. 파일 메타 정보 조회
                FileMetadataDTO fileMetadataDTO = minioUtil.getFileMetadata(objectName, BUCKET_NAME);

                if (fileMetadataDTO != null) {
                    String loginId = securityService.getLoginId();

                    // 2. mongoDB 저장
                    FileMetadata fileMetadata = new FileMetadata();
                    fileMetadata.setId(idGenerateService.generateId());
                    fileMetadata.setBucketName(fileMetadataDTO.getBucketName());
                    fileMetadata.setFileName(objectName);
                    fileMetadata.setFileSize(fileMetadataDTO.getFileSize());
                    fileMetadata.setContentType(fileMetadataDTO.getContentType());
                    fileMetadata.setWorkspaceId(workspaceId);
                    fileMetadata.setDirectoryId(directoryId);
                    fileMetadata.setRegisterid(loginId);
                    fileMetadata.setModifyDatetime(fileMetadataDTO.getModifyDatetime());

                    fileMetadataRepository.save(fileMetadata);

                    // 3. kafka 메세지 전송
                    try {
                        KafkaMessageDTO kafkaMessageDTO = new KafkaMessageDTO();
                        kafkaMessageDTO.setBucketName(BUCKET_NAME);
                        kafkaMessageDTO.setObjectName(objectName);
                        kafkaMessageDTO.setFileId(fileMetadata.getId());
                        kafkaMessageDTO.setDetect(detect);
                        kafkaMessageDTO.setText(text);
    
                        ObjectMapper objectMapper = new ObjectMapper();
                        String kafkaMessage = objectMapper.writeValueAsString(kafkaMessageDTO);

                        if (fileMetadata.getContentType().contains("image")) {
                            kafkaProducerService.sendMessage("image-model", kafkaMessage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                }

            }
        }

        return ResponseEntity.ok("success upload");
    }

}
