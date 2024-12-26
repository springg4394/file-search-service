package com.file.search.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.search.service.dto.workspace.WorkspaceIdDTO;
import com.file.search.service.dto.workspaceMember.WorkspaceMemberListResponseDTO;
import com.file.search.service.dto.workspaceMember.WorkspaceMemberSaveDTO;
import com.file.search.service.model.WorkspaceMemberEntity;
import com.file.search.service.model.WorkspaceMemberId;
import com.file.search.service.service.WorkspaceMemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/workspace")
@RequiredArgsConstructor
public class WorkspaceMemberController {

    private final WorkspaceMemberService workspaceMemberService;

    @PostMapping("/getListWorkspaceMember")
    public List<WorkspaceMemberListResponseDTO> getListWorkspaceMember(@RequestBody WorkspaceIdDTO workspaceIdDTO) {
        return workspaceMemberService.selectWorkspaceMemberList(workspaceIdDTO);
    }

    @PostMapping("/createWorkspaceMember")
    public WorkspaceMemberSaveDTO createWorkspaceMember(@RequestBody WorkspaceMemberSaveDTO workspaceMemberSaveDTO) {
        WorkspaceMemberEntity workspaceMemberEntity = workspaceMemberService.saveWorkspaceMember(workspaceMemberSaveDTO);
        return WorkspaceMemberSaveDTO.builder()
                .memberId(workspaceMemberEntity.getWorkspaceMemberId().getMemberId())
                .workspaceId(workspaceMemberEntity.getWorkspaceMemberId().getWorkspaceId())
                .roleType(workspaceMemberEntity.getRoleType())
                .build();
    }

    @PostMapping("/updateWorkspaceMember")
    public WorkspaceMemberSaveDTO updateWorkspaceMember(@RequestBody WorkspaceMemberSaveDTO workspaceMemberSaveDTO) {
        WorkspaceMemberEntity workspaceMemberEntity = workspaceMemberService.updateWorkspaceMember(workspaceMemberSaveDTO);
        return WorkspaceMemberSaveDTO.builder()
                .memberId(workspaceMemberEntity.getWorkspaceMemberId().getMemberId())
                .workspaceId(workspaceMemberEntity.getWorkspaceMemberId().getWorkspaceId())
                .roleType(workspaceMemberEntity.getRoleType())
                .build(); 
    }

    @PostMapping("/deleteWorkspaceMember")
    public WorkspaceMemberId deleteWorkspaceMember(@RequestBody WorkspaceMemberId workspaceMemberId) {
        return workspaceMemberService.deleteWorkspaceMember(workspaceMemberId);
    }

}
