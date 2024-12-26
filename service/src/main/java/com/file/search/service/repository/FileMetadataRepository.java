package com.file.search.service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.file.search.service.model.FileMetadata;

@Repository
public interface FileMetadataRepository extends MongoRepository<FileMetadata, String> {

    // 1. workspaceId, directoryId 주어짐
    List<FileMetadata> findByWorkspaceIdAndDirectoryId(String workspaceId, String directoryId);
    
    // 2. workspaceId만 주어짐
    List<FileMetadata> findByWorkspaceId(String workspaceId);
    
    // 3. directoryId 주어짐
    List<FileMetadata> findByDirectoryId(String directoryId);
    
    // 4. registerId만 주어짐
    List<FileMetadata> findByRegisterid(String registerId);

}
