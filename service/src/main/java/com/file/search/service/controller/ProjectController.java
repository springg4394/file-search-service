package com.file.search.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.search.service.dto.project.ProjectListResponseDTO;
import com.file.search.service.dto.project.ProjectSaveRequestDTO;
import com.file.search.service.dto.project.ProjectUpdateRequestDTO;
import com.file.search.service.dto.workspace.WorkspaceIdDTO;
import com.file.search.service.model.ProjectEntity;
import com.file.search.service.model.ProjectId;
import com.file.search.service.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/getListProject")
    public List<ProjectListResponseDTO> getListProject(@RequestBody WorkspaceIdDTO workspaceIdDTO) {
        return projectService.selectProjectList(workspaceIdDTO);
    }

    @PostMapping("/createProject")
    public ProjectId createProject(@RequestBody ProjectSaveRequestDTO projectSaveRequestDTO) {
        ProjectEntity projectEntity = projectService.saveProject(projectSaveRequestDTO); 
        return ProjectId.builder()
                .projectId(projectEntity.getProjectId().getProjectId())
                .workspaceId(projectEntity.getProjectId().getWorkspaceId())
                .build();
    }

    @PostMapping("/updateProject")
    public ProjectId updateProject(@RequestBody ProjectUpdateRequestDTO projectUpdateRequestDTO) {
        ProjectEntity projectEntity = projectService.updateProject(projectUpdateRequestDTO);
        return ProjectId.builder()
                .projectId(projectEntity.getProjectId().getProjectId())
                .workspaceId(projectEntity.getProjectId().getWorkspaceId())
                .build(); 
    }

    @PostMapping("/deleteProject")
    public ProjectId deleteProject(@RequestBody ProjectId projectId) {
        return projectService.deleteProject(projectId);
    }

}
