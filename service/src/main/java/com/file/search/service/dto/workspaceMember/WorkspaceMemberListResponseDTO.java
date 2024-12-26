package com.file.search.service.dto.workspaceMember;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceMemberListResponseDTO {
    private String memberId;
    private String memberName;
    private String workspaceId;
    private String workspaceName;
    private String roleType;
    private String registerId;
    private LocalDateTime registerDatetime;
}
