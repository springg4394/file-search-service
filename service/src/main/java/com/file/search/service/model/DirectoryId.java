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
public class DirectoryId implements Serializable {

    @Column(name = "directory_id", columnDefinition = "varchar(12)", nullable = false)
    private String directoryId;

    @Column(name = "workspace_id", columnDefinition = "varchar(12)", nullable = false)
    private String workspaceId;

    @Builder
    public DirectoryId(String directoryId, String workspaceId) {
        this.directoryId = directoryId;
        this.workspaceId = workspaceId;
    }

}
