package com.file.search.service.dto.workspace;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceListResponseDTO {
    private String workspaceId;
    private String workspaceName;
    private String registerId;
    private LocalDateTime registerDatetime;

}
