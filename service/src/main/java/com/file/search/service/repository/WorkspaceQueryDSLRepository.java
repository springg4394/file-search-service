package com.file.search.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;

import com.file.search.service.dto.workspace.WorkspaceListResponseDTO;
import com.file.search.service.model.QWorkspaceEntity;
import com.file.search.service.model.QWorkspaceMemberEntity;
import com.file.search.service.model.WorkspaceEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

@Service
public class WorkspaceQueryDSLRepository extends QuerydslRepositorySupport {

    public WorkspaceQueryDSLRepository() {
        super(WorkspaceEntity.class);
    }

    public List<WorkspaceListResponseDTO> findWorkspaceByMemberId(String memberId) {
        QWorkspaceEntity workspace = QWorkspaceEntity.workspaceEntity;
        QWorkspaceMemberEntity workspaceMember = QWorkspaceMemberEntity.workspaceMemberEntity;

        JPAQuery<WorkspaceListResponseDTO> query = new JPAQuery<>(getEntityManager());

        return query.select(Projections.constructor(WorkspaceListResponseDTO.class,
                        workspace.workspaceId,
                        workspace.workspaceName,
                        workspace.registerId,
                        workspace.registerDatetime))
                    .from(workspaceMember)
                    .leftJoin(workspace).on(workspace.workspaceId.eq(workspaceMember.workspaceMemberId.workspaceId))
                    .where(workspaceMember.workspaceMemberId.memberId.eq(memberId))
                    .fetch();
    }

}
