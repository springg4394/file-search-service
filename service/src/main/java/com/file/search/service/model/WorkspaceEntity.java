package com.file.search.service.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "workspace")
@Getter
@Setter
@NoArgsConstructor
public class WorkspaceEntity {

    @Id
    @Column(name = "workspace_id", columnDefinition = "varchar(12)", nullable = false)
    private String workspaceId;

    @Column(name = "workspace_name", columnDefinition = "varchar(200)", nullable = false)
    private String workspaceName;

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

    @OneToMany(mappedBy = "workspaceEntity")
    private List<ProjectEntity> projectEntity;

    @Builder
    public WorkspaceEntity(String workspaceId, String workspaceName, String registerId, String modifyId) {
        this.workspaceId = workspaceId;
        this.workspaceName = workspaceName;
        this.registerId = registerId;
        this.modifyId = modifyId;
    }

}
