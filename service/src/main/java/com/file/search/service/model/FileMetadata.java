package com.file.search.service.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileMetadata {
    @Id
    private String id;
    private String bucketName;
    private String fileName;
    private Long fileSize;
    private String contentType;
    private String workspaceId;
    private String directoryId;
    private String registerid;
    private LocalDateTime modifyDatetime;

}
