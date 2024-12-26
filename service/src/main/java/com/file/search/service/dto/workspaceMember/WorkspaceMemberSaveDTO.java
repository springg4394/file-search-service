package com.file.search.service.dto.workspaceMember;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WorkspaceMemberSaveDTO {
    private String memberId;
    private String workspaceId;
    private String roleType;

}
