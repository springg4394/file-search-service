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
@Table(name = "directory")
@Getter
@Setter
@NoArgsConstructor
public class DirectoryEntity {

    @EmbeddedId
    private DirectoryId directoryId;

    @ManyToOne
    @JoinColumn(name = "workspace_id", insertable = false, updatable = false)
    private WorkspaceEntity workspaceEntity;

    @Column(name = "directory_name", columnDefinition = "varchar(200)", nullable = false)
    private String directoryName;

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
    public DirectoryEntity(DirectoryId directoryId, String directoryName, String registerId, String modifyId) {
        this.directoryId = directoryId;
        this.directoryName = directoryName;
        this.registerId = registerId;
        this.modifyId = modifyId;
    }

}
