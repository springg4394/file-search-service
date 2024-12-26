package com.file.search.service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.file.search.service.dto.directory.DirectoryListResponseDTO;
import com.file.search.service.dto.directory.DirectorySaveRequestDTO;
import com.file.search.service.dto.directory.DirectoryUpdateRequestDTO;
import com.file.search.service.dto.workspace.WorkspaceIdDTO;
import com.file.search.service.model.DirectoryEntity;
import com.file.search.service.model.DirectoryId;
import com.file.search.service.repository.DirectoryQueryDSLRepository;
import com.file.search.service.repository.DirectoryRepository;
import com.file.search.service.service.common.IdGenerateService;
import com.file.search.service.service.common.SecurityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DirectoryService {

    private final DirectoryRepository         directoryRepository;
    private final DirectoryQueryDSLRepository directoryQueryDSLRepository;
    private final IdGenerateService           idGenerateService;
    private final SecurityService             securityService;
    
    @Transactional(readOnly = true)
    public List<DirectoryListResponseDTO> selectDirectoryList(WorkspaceIdDTO workspaceIdDTO) {
        return directoryQueryDSLRepository.findDirectoryByWorkspaceId(workspaceIdDTO.getWorkspaceId());
    }

    @Transactional
    public DirectoryEntity savaDirectory(DirectorySaveRequestDTO directorySaveRequestDTO) {
        String loginId = securityService.getLoginId();
        
        DirectoryId directoryId = DirectoryId.builder()
            .directoryId(idGenerateService.generateId())
            .workspaceId(directorySaveRequestDTO.getWorkspaceId())
            .build();

        DirectoryEntity directoryEntity = DirectoryEntity.builder()
            .directoryId(directoryId)
            .directoryName(directorySaveRequestDTO.getDirectoryName())
            .registerId(loginId)
            .build();

        return directoryRepository.save(directoryEntity);
    }

    @Transactional
    public DirectoryEntity updateDirectory(DirectoryUpdateRequestDTO directoryUpdateRequestDTO) {
        String loginId = securityService.getLoginId();
        
        DirectoryEntity directoryEntity = directoryRepository.findByDirectoryId_DirectoryIdAndDirectoryId_WorkspaceId(
            directoryUpdateRequestDTO.getDirectoryId(),
            directoryUpdateRequestDTO.getWorkspaceId()
        ).orElseThrow(() -> new RuntimeException("Directory not found"));

        DirectoryId directoryId = DirectoryId.builder()
            .directoryId(directoryEntity.getDirectoryId().getDirectoryId())
            .workspaceId(directoryEntity.getDirectoryId().getWorkspaceId())
            .build();
        
        DirectoryEntity updateDirectoryEntity = DirectoryEntity.builder()
            .directoryId(directoryId)
            .directoryName(Optional.ofNullable(directoryUpdateRequestDTO.getDirectoryName()).orElse(directoryEntity.getDirectoryName()))
            .modifyId(loginId)
            .build();

        return directoryRepository.save(updateDirectoryEntity);
    }

    @Transactional
    public DirectoryId deleteDirectory(DirectoryId directoryId) {
        DirectoryEntity directoryEntity = directoryRepository.findByDirectoryId_DirectoryIdAndDirectoryId_WorkspaceId(
            directoryId.getDirectoryId(),
            directoryId.getWorkspaceId()
        ).orElseThrow(() -> new RuntimeException("Directory not found"));

        directoryRepository.deleteByDirectoryId_DirectoryIdAndDirectoryId_WorkspaceId(
            directoryId.getDirectoryId(),
            directoryId.getWorkspaceId()
        );
        return DirectoryId.builder()
            .directoryId(directoryEntity.getDirectoryId().getDirectoryId())
            .workspaceId(directoryEntity.getDirectoryId().getWorkspaceId())
            .build();
    }

}
