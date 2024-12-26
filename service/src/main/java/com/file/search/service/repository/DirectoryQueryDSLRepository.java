package com.file.search.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;

import com.file.search.service.dto.directory.DirectoryListResponseDTO;
import com.file.search.service.model.DirectoryEntity;
import com.file.search.service.model.QDirectoryEntity;
import com.file.search.service.model.QWorkspaceEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

@Service
public class DirectoryQueryDSLRepository extends QuerydslRepositorySupport {
    
    public DirectoryQueryDSLRepository() {
        super(DirectoryEntity.class);
    }

    public List<DirectoryListResponseDTO> findDirectoryByWorkspaceId(String workspaceId) {
        QDirectoryEntity directory = QDirectoryEntity.directoryEntity;
        QWorkspaceEntity workspace = QWorkspaceEntity.workspaceEntity;

        JPAQuery<DirectoryListResponseDTO> query = new JPAQuery<>(getEntityManager());

        return query.select(Projections.constructor(DirectoryListResponseDTO.class,
                        directory.directoryId.directoryId,
                        workspace.workspaceId,
                        workspace.workspaceName,
                        directory.directoryName,
                        directory.registerId,
                        directory.registerDatetime))
                    .from(directory)
                    .join(workspace).on(workspace.workspaceId.eq(directory.directoryId.workspaceId))
                    .where(workspace.workspaceId.eq(workspaceId))
                    .fetch();
    }

}

