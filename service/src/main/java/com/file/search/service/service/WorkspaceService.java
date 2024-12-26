package com.file.search.service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.file.search.service.dto.workspace.WorkspaceIdDTO;
import com.file.search.service.dto.workspace.WorkspaceListResponseDTO;
import com.file.search.service.dto.workspace.WorkspaceSaveRequestDTO;
import com.file.search.service.dto.workspace.WorkspaceUpdateRequestDTO;
import com.file.search.service.model.WorkspaceEntity;
import com.file.search.service.repository.WorkspaceQueryDSLRepository;
import com.file.search.service.repository.WorkspaceRepository;
import com.file.search.service.service.common.IdGenerateService;
import com.file.search.service.service.common.SecurityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository         workspaceRepository;
    private final WorkspaceQueryDSLRepository workspaceQueryDSLRepository;
    private final IdGenerateService           idGenerateService;
    private final WorkspaceMemberService      workspaceMemberService;
    private final SecurityService             securityService;

    @Transactional(readOnly = true)
    public List<WorkspaceListResponseDTO> selectWorkspaceList(String memberId) {
        return workspaceQueryDSLRepository.findWorkspaceByMemberId(memberId);
    }

    @Transactional
    public WorkspaceEntity saveWorkspace(WorkspaceSaveRequestDTO workspaceSaveRequestDTO) {
        String loginId = securityService.getLoginId();
        
        WorkspaceEntity workspaceEntity = WorkspaceEntity.builder()
            .workspaceId(idGenerateService.generateId())
            .workspaceName(workspaceSaveRequestDTO.getWorkspaceName())
            .registerId(loginId)
            .build();

        return workspaceRepository.save(workspaceEntity);
    }

    @Transactional
    public WorkspaceEntity updateWorkspace(WorkspaceUpdateRequestDTO workspaceUpdateRequestDTO) {
        String loginId = securityService.getLoginId();
        
        WorkspaceEntity workspaceEntity = workspaceRepository.findById(workspaceUpdateRequestDTO.getWorkspaceId())
            .orElseThrow(() -> new RuntimeException("Workspace not found"));

        WorkspaceEntity updateWorkspaceEntity = WorkspaceEntity.builder()
            .workspaceId(workspaceEntity.getWorkspaceId())
            .workspaceName(Optional.ofNullable(workspaceUpdateRequestDTO.getWorkspaceName()).orElse(workspaceEntity.getWorkspaceName()))
            .modifyId(loginId)
            .build();

        return workspaceRepository.save(updateWorkspaceEntity);
    }

    @Transactional
    public WorkspaceIdDTO deleteWorkspace(WorkspaceIdDTO workspaceIdDTO) {
        WorkspaceEntity workspaceEntity = workspaceRepository.findById(workspaceIdDTO.getWorkspaceId())
            .orElseThrow(() -> new RuntimeException("Workspace not found"));

        // workspace member 삭제
        workspaceMemberService.deleteWorkspaceByWorkspaceId(workspaceEntity.getWorkspaceId());
        
        // workspace 삭제
        workspaceRepository.deleteById(workspaceEntity.getWorkspaceId());

        return WorkspaceIdDTO.builder()
                    .workspaceId(workspaceEntity.getWorkspaceId())
                    .build();
    }

}
