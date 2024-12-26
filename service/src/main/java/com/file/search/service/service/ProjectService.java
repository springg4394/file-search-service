package com.file.search.service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.file.search.service.dto.project.ProjectListResponseDTO;
import com.file.search.service.dto.project.ProjectSaveRequestDTO;
import com.file.search.service.dto.project.ProjectUpdateRequestDTO;
import com.file.search.service.dto.workspace.WorkspaceIdDTO;
import com.file.search.service.model.ProjectEntity;
import com.file.search.service.model.ProjectId;
import com.file.search.service.repository.ProjectQueryDSLRepository;
import com.file.search.service.repository.ProjectRepository;
import com.file.search.service.service.common.IdGenerateService;
import com.file.search.service.service.common.SecurityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository         projectRepository;
    private final ProjectQueryDSLRepository projectQueryDSLRepository;
    private final IdGenerateService         idGenerateService;
    private final SecurityService           securityService;
    
    @Transactional(readOnly = true)
    public List<ProjectListResponseDTO> selectProjectList(WorkspaceIdDTO workspaceIdDTO) {
        return projectQueryDSLRepository.findProjectByWorkspaceId(workspaceIdDTO.getWorkspaceId());
    }

    @Transactional
    public ProjectEntity saveProject(ProjectSaveRequestDTO projectSaveRequestDTO) {
        String loginId = securityService.getLoginId();
        
        ProjectId projectId = ProjectId.builder()
            .projectId(idGenerateService.generateId())
            .workspaceId(projectSaveRequestDTO.getWorkspaceId())
            .build();

        ProjectEntity projectEntity = ProjectEntity.builder()
            .projectId(projectId)
            .projectType(projectSaveRequestDTO.getProjectType())
            .projectName(projectSaveRequestDTO.getProjectName())
            .registerId(loginId)
            .build();

        return projectRepository.save(projectEntity);
    }

    @Transactional
    public ProjectEntity updateProject(ProjectUpdateRequestDTO projectUpdateRequestDTO) {
        String loginId = securityService.getLoginId();
        
        ProjectEntity projectEntity = projectRepository.findByProjectId_ProjectIdAndProjectId_WorkspaceId(
            projectUpdateRequestDTO.getProjectId(),
            projectUpdateRequestDTO.getWorkspaceId()
        ).orElseThrow(() -> new RuntimeException("Project not found"));

        ProjectId projectId = ProjectId.builder()
            .projectId(projectEntity.getProjectId().getProjectId())
            .workspaceId(projectEntity.getProjectId().getWorkspaceId())
            .build();

        ProjectEntity updateProjectEntity = ProjectEntity.builder()
            .projectId(projectId)
            .projectType(Optional.ofNullable(projectUpdateRequestDTO.getProjectType()).orElse(projectEntity.getProjectType()))
            .projectName(Optional.ofNullable(projectUpdateRequestDTO.getProjectName()).orElse(projectEntity.getProjectName()))
            .modifyId(loginId)
            .build();

        return projectRepository.save(updateProjectEntity);
    }

    @Transactional
    public ProjectId deleteProject(ProjectId projectId) {
        ProjectEntity projectEntity = projectRepository.findByProjectId_ProjectIdAndProjectId_WorkspaceId(
            projectId.getProjectId(),
            projectId.getWorkspaceId()
        ).orElseThrow(() -> new RuntimeException("Project not found"));

        projectRepository.deleteByProjectId_ProjectIdAndProjectId_WorkspaceId(
            projectId.getProjectId(),
            projectId.getWorkspaceId()
        );
        return ProjectId.builder()
                .projectId(projectEntity.getProjectId().getProjectId())
                .workspaceId(projectEntity.getProjectId().getWorkspaceId())
                .build();
    }

}