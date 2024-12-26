package com.file.search.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;

import com.file.search.service.dto.projectMember.ProjectMemberListResponseDTO;
import com.file.search.service.model.ProjectId;
import com.file.search.service.model.ProjectMemberEntity;
import com.file.search.service.model.QMemberEntity;
import com.file.search.service.model.QProjectEntity;
import com.file.search.service.model.QProjectMemberEntity;
import com.file.search.service.model.QProjectMemberId;
import com.file.search.service.model.QWorkspaceEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

@Service
public class ProjectMemberQueryDSLRepository extends QuerydslRepositorySupport {

    public ProjectMemberQueryDSLRepository() {
        super(ProjectMemberEntity.class);
    }

    public List<ProjectMemberListResponseDTO> findProjectMemberByProjectId(ProjectId projectId) {
        QProjectMemberEntity projectMember = QProjectMemberEntity.projectMemberEntity;
        QProjectMemberId projectMemberId = projectMember.projectMemberId;
        QMemberEntity member = QMemberEntity.memberEntity;
        QProjectEntity project = QProjectEntity.projectEntity;
        QWorkspaceEntity workspace = QWorkspaceEntity.workspaceEntity;

        JPAQuery<ProjectMemberListResponseDTO> query = new JPAQuery<>(getEntityManager());

        return query.select(Projections.constructor(ProjectMemberListResponseDTO.class,
                        member.memberId,
                        member.memberName,
                        workspace.workspaceId,
                        workspace.workspaceName,
                        project.projectId.projectId,
                        project.projectName,
                        projectMember.roleType,
                        projectMember.registerId,
                        projectMember.registerDatetime))
                    .from(projectMember)
                    .join(projectMember.projectEntity, project)
                    .join(project.workspaceEntity, workspace)
                    .join(member).on(member.memberId.eq(projectMemberId.memberId))
                    .where(project.projectId.eq(projectId))
                    .fetch();
    }

}
