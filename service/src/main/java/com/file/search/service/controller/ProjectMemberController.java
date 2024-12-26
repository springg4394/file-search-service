package com.file.search.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.search.service.dto.projectMember.ProjectMemberListResponseDTO;
import com.file.search.service.dto.projectMember.ProjectMemberSaveRequestDTO;
import com.file.search.service.model.ProjectId;
import com.file.search.service.model.ProjectMemberEntity;
import com.file.search.service.model.ProjectMemberId;
import com.file.search.service.service.ProjectMemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @PostMapping("/getListProjectMember")
    public List<ProjectMemberListResponseDTO> getListProjectMember(@RequestBody ProjectId projectId) {
        return projectMemberService.selectProjectMemberList(projectId);
    }

    @PostMapping("/createProjectMember")
    public ProjectMemberId createProjectMember(@RequestBody ProjectMemberSaveRequestDTO projectMemberSaveRequestDTO) {
        ProjectMemberEntity projectMemberEntity = projectMemberService.saveProjectMember(projectMemberSaveRequestDTO);
        return ProjectMemberId.builder()
                .memberId(projectMemberEntity.getProjectMemberId().getMemberId())
                .workspaceId(projectMemberEntity.getProjectMemberId().getWorkspaceId())
                .projectId(projectMemberEntity.getProjectMemberId().getProjectId())
                .build();
    }

    @PostMapping("/updateProjectMember")
    public ProjectMemberId updateProjectMember(@RequestBody ProjectMemberSaveRequestDTO projectMemberSaveRequestDTO) {
        ProjectMemberEntity projectMemberEntity = projectMemberService.updateProjectMember(projectMemberSaveRequestDTO);
        return ProjectMemberId.builder()
                .memberId(projectMemberEntity.getProjectMemberId().getMemberId())
                .workspaceId(projectMemberEntity.getProjectMemberId().getWorkspaceId())
                .projectId(projectMemberEntity.getProjectMemberId().getProjectId())
                .build(); 
    }

    @PostMapping("/deleteProjectMember")
    public ProjectMemberId deleteProjectMember(@RequestBody ProjectMemberId projectMemberId) {        
        return projectMemberService.deleteProjectMember(projectMemberId);
    }

}
