package com.file.search.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.file.search.service.model.DirectoryEntity;
import com.file.search.service.model.DirectoryId;

@Repository
public interface DirectoryRepository extends JpaRepository<DirectoryEntity, DirectoryId> {

    Optional<DirectoryEntity> findByDirectoryId_DirectoryIdAndDirectoryId_WorkspaceId(String directoryId, String workspaceId);

    void deleteByDirectoryId_DirectoryIdAndDirectoryId_WorkspaceId(String directoryId, String workspaceId);

}
