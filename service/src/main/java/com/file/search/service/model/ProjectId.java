package com.file.search.service.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class ProjectId implements Serializable {

    @Column(name = "project_id", columnDefinition = "varchar(12)", nullable = false)
    private String projectId;

    @Column(name = "workspace_id", columnDefinition = "varchar(12)", nullable = false)
    private String workspaceId;

    @Builder
    public ProjectId(String projectId, String workspaceId) {
        this.projectId = projectId;
        this.workspaceId = workspaceId;
    }

}