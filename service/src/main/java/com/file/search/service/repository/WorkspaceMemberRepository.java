package com.file.search.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.file.search.service.model.WorkspaceMemberEntity;
import com.file.search.service.model.WorkspaceMemberId;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMemberEntity, WorkspaceMemberId> {

    Optional<WorkspaceMemberEntity> findByWorkspaceMemberId_WorkspaceIdAndWorkspaceMemberId_MemberId(String workspaceId, String memberId);

    void deleteByWorkspaceMemberId_WorkspaceIdAndWorkspaceMemberId_MemberId(String workspaceId, String memberId);

    void deleteAllByWorkspaceMemberId_WorkspaceId(String workspaceId);

}
