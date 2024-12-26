package com.file.search.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;

import com.file.search.service.dto.workspaceMember.WorkspaceMemberListResponseDTO;
import com.file.search.service.model.QMemberEntity;
import com.file.search.service.model.QWorkspaceEntity;
import com.file.search.service.model.QWorkspaceMemberEntity;
import com.file.search.service.model.QWorkspaceMemberId;
import com.file.search.service.model.WorkspaceMemberEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

@Service
public class WorkspaceMemberQueryDSLRepository extends QuerydslRepositorySupport {

    public WorkspaceMemberQueryDSLRepository() {
        super(WorkspaceMemberEntity.class);
    }

    public List<WorkspaceMemberListResponseDTO> findWorkspaceMembersByWorkspaceId(String workspaceId) {
        QWorkspaceMemberEntity workspaceMember = QWorkspaceMemberEntity.workspaceMemberEntity;
        QWorkspaceMemberId workspaceMemberId = workspaceMember.workspaceMemberId;
        QMemberEntity member = QMemberEntity.memberEntity;
        QWorkspaceEntity workspace = QWorkspaceEntity.workspaceEntity;
        
        JPAQuery<WorkspaceMemberListResponseDTO> query = new JPAQuery<>(getEntityManager());

        return query.select(Projections.constructor(WorkspaceMemberListResponseDTO.class,
                        member.memberId,
                        member.memberName,
                        workspace.workspaceId,
                        workspace.workspaceName,
                        workspaceMember.roleType,
                        workspaceMember.registerId,
                        workspaceMember.registerDatetime))
                    .from(workspaceMember)
                    .join(workspaceMember.workspaceEntity, workspace)
                    .join(member).on(member.memberId.eq(workspaceMemberId.memberId)) 
                    .where(workspace.workspaceId.eq(workspaceId))
                    .fetch();
    }

}
