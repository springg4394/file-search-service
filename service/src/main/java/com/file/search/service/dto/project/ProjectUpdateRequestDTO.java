package com.file.search.service.dto.project;

import lombok.Getter;

@Getter
public class ProjectUpdateRequestDTO {
    private String projectId;
    private String workspaceId;
    private String projectType;
    private String projectName;

}
