package com.file.search.service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.file.search.service.dto.workspace.WorkspaceIdDTO;
import com.file.search.service.dto.workspaceMember.WorkspaceMemberListResponseDTO;
import com.file.search.service.dto.workspaceMember.WorkspaceMemberSaveDTO;
import com.file.search.service.model.WorkspaceMemberEntity;
import com.file.search.service.model.WorkspaceMemberId;
import com.file.search.service.repository.WorkspaceMemberQueryDSLRepository;
import com.file.search.service.repository.WorkspaceMemberRepository;
import com.file.search.service.service.common.SecurityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkspaceMemberService {

    private final WorkspaceMemberRepository         workspaceMemberRepository;
    private final WorkspaceMemberQueryDSLRepository workspaceMemberQueryDSLRepository;
    private final SecurityService                   securityService;

    @Transactional(readOnly = true)
    public List<WorkspaceMemberListResponseDTO> selectWorkspaceMemberList(WorkspaceIdDTO workspaceIdDTO) {
        return workspaceMemberQueryDSLRepository.findWorkspaceMembersByWorkspaceId(workspaceIdDTO.getWorkspaceId());
    }

    @Transactional
    public WorkspaceMemberEntity saveWorkspaceMember(WorkspaceMemberSaveDTO workspaceMemberSaveDTO) {
        String loginId = securityService.getLoginId();
        
        WorkspaceMemberId workspaceMemberId = WorkspaceMemberId.builder()
            .memberId(workspaceMemberSaveDTO.getMemberId())
            .workspaceId(workspaceMemberSaveDTO.getWorkspaceId())
            .build();
        
        WorkspaceMemberEntity workspaceMemberEntity = WorkspaceMemberEntity.builder()
            .workspaceMemberId(workspaceMemberId)
            .roleType(workspaceMemberSaveDTO.getRoleType())
            .registerId(loginId)
            .build();
        
        return workspaceMemberRepository.save(workspaceMemberEntity);
    }

    @Transactional
    public WorkspaceMemberEntity updateWorkspaceMember(WorkspaceMemberSaveDTO workspaceMemberSaveDTO) {
        String loginId = securityService.getLoginId();
        
        WorkspaceMemberEntity workspaceMemberEntity = workspaceMemberRepository.findByWorkspaceMemberId_WorkspaceIdAndWorkspaceMemberId_MemberId(
            workspaceMemberSaveDTO.getWorkspaceId(),
            workspaceMemberSaveDTO.getMemberId()
        ).orElseThrow(() -> new RuntimeException("WorkspaceMember not found"));

        WorkspaceMemberId workspaceMemberId = WorkspaceMemberId.builder()
            .memberId(workspaceMemberEntity.getWorkspaceMemberId().getMemberId())
            .workspaceId(workspaceMemberEntity.getWorkspaceMemberId().getWorkspaceId())
            .build();

        WorkspaceMemberEntity updateWorkspaceMemberEntity = WorkspaceMemberEntity.builder()
            .workspaceMemberId(workspaceMemberId)
            .roleType(Optional.ofNullable(workspaceMemberSaveDTO.getRoleType()).orElse(workspaceMemberEntity.getRoleType()))
            .modifyId(loginId)
            .build();

        return workspaceMemberRepository.save(updateWorkspaceMemberEntity);
    }

    @Transactional
	public WorkspaceMemberId deleteWorkspaceMember(WorkspaceMemberId workspaceMemberId) {
		WorkspaceMemberEntity workspaceMemberEntity = workspaceMemberRepository.findByWorkspaceMemberId_WorkspaceIdAndWorkspaceMemberId_MemberId(
            workspaceMemberId.getWorkspaceId(),
            workspaceMemberId.getMemberId()
        ).orElseThrow(() -> new RuntimeException("WorkspaceMember not found"));
        
        workspaceMemberRepository.deleteByWorkspaceMemberId_WorkspaceIdAndWorkspaceMemberId_MemberId(
            workspaceMemberEntity.getWorkspaceMemberId().getWorkspaceId(),
            workspaceMemberEntity.getWorkspaceMemberId().getMemberId()
        );
        return WorkspaceMemberId.builder()
                .memberId(workspaceMemberEntity.getWorkspaceMemberId().getMemberId())
                .workspaceId(workspaceMemberEntity.getWorkspaceMemberId().getWorkspaceId())
                .build();
	}

    @Transactional
    public void deleteWorkspaceByWorkspaceId(String workspaceId) {
        workspaceMemberRepository.deleteAllByWorkspaceMemberId_WorkspaceId(workspaceId);
    }

}
