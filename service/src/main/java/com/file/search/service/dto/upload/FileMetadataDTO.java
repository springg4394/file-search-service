package com.file.search.service.dto.upload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileMetadataDTO {
    private String bucketName;
    private String fileName;
    private Long fileSize;
    private String contentType;
    private LocalDateTime modifyDatetime;
}
