package com.file.search.service.dto.auth;

import com.file.search.service.model.MemberEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private Boolean result;
    private String message;
    private MemberEntity memberInfo; 

}
