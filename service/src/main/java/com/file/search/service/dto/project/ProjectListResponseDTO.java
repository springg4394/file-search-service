package com.file.search.service.dto.project;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectListResponseDTO {
    private String projectId;
    private String workspaceId;
    private String workspaceName;
    private String projectType;
    private String projectName;
    private String registerId;
    private LocalDateTime registerDatetime;
}
