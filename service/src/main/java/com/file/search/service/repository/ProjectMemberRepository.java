package com.file.search.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.file.search.service.model.ProjectMemberEntity;
import com.file.search.service.model.ProjectMemberId;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMemberEntity, ProjectMemberId> {

    Optional<ProjectMemberEntity> findByProjectMemberId_WorkspaceIdAndProjectMemberId_ProjectIdAndProjectMemberId_MemberId(String workspaceId,
            String projectId, String memberId);

    void deleteByProjectMemberId_WorkspaceIdAndProjectMemberId_ProjectIdAndProjectMemberId_MemberId(String workspaceId,
            String projectId, String memberId);

}
