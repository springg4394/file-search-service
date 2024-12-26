package com.file.search.service.dto.member;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberListResponseDTO {
    private String memberId;
    private String memberName;
    private String registerId;
    private LocalDateTime registerDatetime;

}
