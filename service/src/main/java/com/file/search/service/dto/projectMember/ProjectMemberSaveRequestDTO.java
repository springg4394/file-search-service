package com.file.search.service.dto.projectMember;

import lombok.Getter;

@Getter
public class ProjectMemberSaveRequestDTO {
    private String memberId;
    private String workspaceId;
    private String projectId;
    private String roleType;

}
