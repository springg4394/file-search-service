package com.file.search.service.dto.project;

import lombok.Getter;

@Getter
public class ProjectSaveRequestDTO {
    private String workspaceId;
    private String projectType;
    private String projectName;

}
