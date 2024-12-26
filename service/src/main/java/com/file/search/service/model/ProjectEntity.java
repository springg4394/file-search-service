package com.file.search.service.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
public class ProjectEntity {

    @EmbeddedId
    private ProjectId projectId;

    @ManyToOne
    @JoinColumn(name = "workspace_id", insertable = false, updatable = false)
    private WorkspaceEntity workspaceEntity;

    @Column(name = "project_type", columnDefinition = "varchar(20)", nullable = false)
    private String projectType;

    @Column(name = "project_name", columnDefinition = "varchar(200)", nullable = false)
    private String projectName;

    @Column(name = "register_id", columnDefinition = "varchar(100)", nullable = false, updatable = false)
    private String registerId;

    @CreatedDate
    @Column(name = "register_datetime", nullable = false, updatable = false)
    private LocalDateTime registerDatetime;

    @Column(name = "modify_id", columnDefinition = "varchar(100)")
    private String modifyId;

    @LastModifiedDate
    @Column(name = "modify_datetime")
    private LocalDateTime modifyDatetime;

    @Builder
    public ProjectEntity(ProjectId projectId, String projectType, String projectName, String registerId, String modifyId) {
        this.projectId = projectId;
        this.projectType = projectType;
        this.projectName = projectName;
        this.registerId = registerId;
        this.modifyId = modifyId;
    }

}
