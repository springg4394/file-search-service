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
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "project_member")
@Getter
@Setter
@NoArgsConstructor
public class ProjectMemberEntity {

    @EmbeddedId
    private ProjectMemberId projectMemberId;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "workspace_id", referencedColumnName = "workspace_id", insertable = false, updatable = false),
        @JoinColumn(name = "project_id", referencedColumnName = "project_id", insertable = false, updatable = false)
    })
    private ProjectEntity projectEntity;

    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private MemberEntity memberEntity;

    @Column(name = "role_type", columnDefinition = "varchar(20)", nullable = false)
    private String roleType;

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
    public ProjectMemberEntity(ProjectMemberId projectMemberId, String roleType, String registerId, String modifyId) {
        this.projectMemberId = projectMemberId;
        this.roleType = roleType;
        this.registerId = registerId;
        this.modifyId = modifyId;
    }

}
