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
public class ProjectMemberId implements Serializable {

    @Column(name = "member_id", columnDefinition = "varchar(100)", nullable = false)
    private String memberId;

    @Column(name = "workspace_id", columnDefinition = "varchar(12)", nullable = false)
    private String workspaceId;

    @Column(name = "project_id", columnDefinition = "varchar(12)", nullable = false)
    private String projectId;

    @Builder
    public ProjectMemberId(String memberId, String workspaceId, String projectId) {
        this.memberId = memberId;
        this.workspaceId = workspaceId;
        this.projectId = projectId;
    }

}
