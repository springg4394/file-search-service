package com.file.search.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.search.service.dto.workspace.WorkspaceIdDTO;
import com.file.search.service.dto.workspace.WorkspaceListResponseDTO;
import com.file.search.service.dto.workspace.WorkspaceSaveRequestDTO;
import com.file.search.service.dto.workspace.WorkspaceUpdateRequestDTO;
import com.file.search.service.dto.workspaceMember.WorkspaceMemberSaveDTO;
import com.file.search.service.model.WorkspaceEntity;
import com.file.search.service.service.WorkspaceMemberService;
import com.file.search.service.service.WorkspaceService;
import com.file.search.service.service.common.SecurityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/workspace")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService       workspaceService;
    private final WorkspaceMemberService workspaceMemberService;
    private final SecurityService        securityService;

    @PostMapping("/getListWorkspace")
    public List<WorkspaceListResponseDTO> getListWorkspace() {
        String loginId = securityService.getLoginId();
        return workspaceService.selectWorkspaceList(loginId);
    }

    @PostMapping("/createWorkspace")
    public WorkspaceIdDTO createWorkspace(@RequestBody WorkspaceSaveRequestDTO workspaceSaveRequestDTO) {
        WorkspaceEntity workspaceEntity = workspaceService.saveWorkspace(workspaceSaveRequestDTO);
        String loginId = securityService.getLoginId();
        
        // workspace 생성자 workspace member로 추가
        if (workspaceEntity != null) {
            WorkspaceMemberSaveDTO workspaceMemberSaveDTO = WorkspaceMemberSaveDTO.builder()
                .memberId(workspaceEntity.getRegisterId())
                .workspaceId(workspaceEntity.getWorkspaceId())
                .roleType(loginId)
                .build();

            workspaceMemberService.saveWorkspaceMember(workspaceMemberSaveDTO);
        }

        return WorkspaceIdDTO.builder()
                .workspaceId(workspaceEntity.getWorkspaceId())
                .build();
    }

    @PostMapping("/updateWorkspace")
    public WorkspaceIdDTO updateWorkspace(@RequestBody WorkspaceUpdateRequestDTO workspaceUpdateRequestDTO) {        
        WorkspaceEntity workspaceEntity = workspaceService.updateWorkspace(workspaceUpdateRequestDTO);
        return WorkspaceIdDTO.builder()
                .workspaceId(workspaceEntity.getWorkspaceId())
                .build(); 
    }
    
    @PostMapping("/deleteWorkspace")
    public WorkspaceIdDTO deleteWorkspace(@RequestBody WorkspaceIdDTO workspaceIdDTO) {
        return workspaceService.deleteWorkspace(workspaceIdDTO);
    }

}
