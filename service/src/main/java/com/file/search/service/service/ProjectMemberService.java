package com.file.search.service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.file.search.service.dto.projectMember.ProjectMemberListResponseDTO;
import com.file.search.service.dto.projectMember.ProjectMemberSaveRequestDTO;
import com.file.search.service.model.ProjectId;
import com.file.search.service.model.ProjectMemberEntity;
import com.file.search.service.model.ProjectMemberId;
import com.file.search.service.repository.ProjectMemberQueryDSLRepository;
import com.file.search.service.repository.ProjectMemberRepository;
import com.file.search.service.service.common.SecurityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectMemberRepository         projectMemberRepository;
    private final ProjectMemberQueryDSLRepository projectMemberQueryDSLRepository;
    private final SecurityService                 securityService;

    @Transactional(readOnly = true)
    public List<ProjectMemberListResponseDTO> selectProjectMemberList(ProjectId projectId) {
        return projectMemberQueryDSLRepository.findProjectMemberByProjectId(projectId);
    }
    
    @Transactional
    public ProjectMemberEntity saveProjectMember(ProjectMemberSaveRequestDTO projectMemberSaveRequestDTO) {
        String loginId = securityService.getLoginId();
        
        ProjectMemberId projectMemberId = ProjectMemberId.builder()
            .memberId(projectMemberSaveRequestDTO.getMemberId())
            .workspaceId(projectMemberSaveRequestDTO.getWorkspaceId())
            .projectId(projectMemberSaveRequestDTO.getProjectId())
            .build();

        ProjectMemberEntity projectMemberEntity = ProjectMemberEntity.builder()
            .projectMemberId(projectMemberId)
            .roleType(projectMemberSaveRequestDTO.getRoleType())
            .registerId(loginId)
            .build();
        return projectMemberRepository.save(projectMemberEntity);
    }

    @Transactional
    public ProjectMemberEntity updateProjectMember(ProjectMemberSaveRequestDTO projectMemberSaveRequestDTO) {
        String loginId = securityService.getLoginId();
        
        ProjectMemberEntity projectMemberEntity = projectMemberRepository.findByProjectMemberId_WorkspaceIdAndProjectMemberId_ProjectIdAndProjectMemberId_MemberId(
            projectMemberSaveRequestDTO.getWorkspaceId(),
            projectMemberSaveRequestDTO.getProjectId(),
            projectMemberSaveRequestDTO.getMemberId()
        ).orElseThrow(() -> new RuntimeException("ProjectMember not found"));

        ProjectMemberId projectMemberId = ProjectMemberId.builder()
            .memberId(projectMemberEntity.getProjectMemberId().getMemberId())
            .workspaceId(projectMemberEntity.getProjectMemberId().getWorkspaceId())
            .projectId(projectMemberEntity.getProjectMemberId().getProjectId())
            .build();
        
        ProjectMemberEntity updateProjectMemberEntity = ProjectMemberEntity.builder()
            .projectMemberId(projectMemberId)
            .roleType(Optional.ofNullable(projectMemberSaveRequestDTO.getRoleType()).orElse(projectMemberEntity.getRoleType()))
            .modifyId(loginId)
            .build();

        return projectMemberRepository.save(updateProjectMemberEntity);
    }

    @Transactional
    public ProjectMemberId deleteProjectMember(ProjectMemberId projectMemberId) {
        ProjectMemberEntity projectMemberEntity = projectMemberRepository.findByProjectMemberId_WorkspaceIdAndProjectMemberId_ProjectIdAndProjectMemberId_MemberId(
            projectMemberId.getWorkspaceId(),
            projectMemberId.getProjectId(),
            projectMemberId.getMemberId()
        ).orElseThrow(() -> new RuntimeException("ProjectMember not found"));

        projectMemberRepository.deleteByProjectMemberId_WorkspaceIdAndProjectMemberId_ProjectIdAndProjectMemberId_MemberId(
            projectMemberEntity.getProjectMemberId().getWorkspaceId(),
            projectMemberEntity.getProjectMemberId().getProjectId(),
            projectMemberEntity.getProjectMemberId().getMemberId()
        );
        
        return ProjectMemberId.builder()
            .memberId(projectMemberEntity.getProjectMemberId().getMemberId())
            .workspaceId(projectMemberEntity.getProjectMemberId().getWorkspaceId())
            .projectId(projectMemberEntity.getProjectMemberId().getProjectId())
            .build();
    }

}
