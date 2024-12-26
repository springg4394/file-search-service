package com.file.search.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;

import com.file.search.service.dto.project.ProjectListResponseDTO;
import com.file.search.service.model.ProjectEntity;
import com.file.search.service.model.QProjectEntity;
import com.file.search.service.model.QWorkspaceEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

@Service
public class ProjectQueryDSLRepository extends QuerydslRepositorySupport {

    public ProjectQueryDSLRepository() {
        super(ProjectEntity.class);
    }

    public List<ProjectListResponseDTO> findProjectByWorkspaceId(String workspaceId) {
        QProjectEntity project = QProjectEntity.projectEntity;
        QWorkspaceEntity workspace = QWorkspaceEntity.workspaceEntity;

        JPAQuery<ProjectListResponseDTO> query = new JPAQuery<>(getEntityManager());

        return query.select(Projections.constructor(ProjectListResponseDTO.class,
                        project.projectId.projectId,
                        workspace.workspaceId,
                        workspace.workspaceName, 
                        project.projectType,
                        project.projectName,
                        project.registerId,
                        project.registerDatetime))
                    .from(project)
                    .join(workspace).on(workspace.workspaceId.eq(project.projectId.workspaceId))
                    .where(workspace.workspaceId.eq(workspaceId))
                    .fetch();
    }

}
