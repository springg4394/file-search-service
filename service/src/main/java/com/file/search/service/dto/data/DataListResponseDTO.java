package com.file.search.service.dto.data;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataListResponseDTO {
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
