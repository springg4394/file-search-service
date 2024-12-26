package com.file.search.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.search.service.dto.directory.DirectoryListResponseDTO;
import com.file.search.service.dto.directory.DirectorySaveRequestDTO;
import com.file.search.service.dto.directory.DirectoryUpdateRequestDTO;
import com.file.search.service.dto.workspace.WorkspaceIdDTO;
import com.file.search.service.model.DirectoryEntity;
import com.file.search.service.model.DirectoryId;
import com.file.search.service.service.DirectoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/directory")
@RequiredArgsConstructor
public class DirectoryController {

    private final DirectoryService directoryService;

    @PostMapping("/getListDirectory")
    public List<DirectoryListResponseDTO> getListDirectory(@RequestBody WorkspaceIdDTO workspaceIdDTO) {
        return directoryService.selectDirectoryList(workspaceIdDTO);
    }

    @PostMapping("/createDirectory")
    public DirectoryId createDirectory(@RequestBody DirectorySaveRequestDTO directorySaveRequestDTO) {
        DirectoryEntity directoryEntity = directoryService.savaDirectory(directorySaveRequestDTO);
        return DirectoryId.builder()
            .directoryId(directoryEntity.getDirectoryId().getDirectoryId())
            .workspaceId(directoryEntity.getDirectoryId().getWorkspaceId())
            .build();
    }

    @PostMapping("/updateDirectory")
    public DirectoryId createDirectory(@RequestBody DirectoryUpdateRequestDTO directoryUpdateRequestDTO) {
        DirectoryEntity directoryEntity = directoryService.updateDirectory(directoryUpdateRequestDTO);
        return DirectoryId.builder()
            .directoryId(directoryEntity.getDirectoryId().getDirectoryId())
            .workspaceId(directoryEntity.getDirectoryId().getWorkspaceId())
            .build();
    }

    @PostMapping("/deleteDirectory")
    public DirectoryId deleteDirectory(@RequestBody DirectoryId directoryId) {
        return directoryService.deleteDirectory(directoryId);
    }

}
