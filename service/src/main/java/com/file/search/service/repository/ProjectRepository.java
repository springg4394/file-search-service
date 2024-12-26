package com.file.search.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.file.search.service.model.ProjectEntity;
import com.file.search.service.model.ProjectId;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, ProjectId> {

    Optional<ProjectEntity> findByProjectId_ProjectIdAndProjectId_WorkspaceId(String projectId, String workspaceId);

    void deleteByProjectId_ProjectIdAndProjectId_WorkspaceId(String projectId, String workspaceId);
}
