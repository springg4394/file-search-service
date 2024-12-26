package com.file.search.service.dto.directory;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectoryListResponseDTO {
    private String directoryId;
    private String workspaceId;
    private String workspaceName;
    private String directoryName;
    private String registerId;
    private LocalDateTime registerDatetime;

}
