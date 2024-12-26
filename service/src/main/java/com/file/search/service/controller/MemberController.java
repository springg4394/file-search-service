package com.file.search.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.search.service.dto.member.MemberDeleteRequestDTO;
import com.file.search.service.dto.member.MemberIdDTO;
import com.file.search.service.dto.member.MemberListResponseDTO;
import com.file.search.service.dto.member.MemberSaveRequestDTO;
import com.file.search.service.model.MemberEntity;
import com.file.search.service.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/getListMember")
    public List<MemberListResponseDTO> getListMember() {
        return memberService.selectMemberList();
    }
    
    @PostMapping("/createMember")
    public MemberIdDTO createMember(@RequestBody MemberSaveRequestDTO memberSaveRequestDTO) {
        MemberEntity memberEntity = memberService.saveMember(memberSaveRequestDTO); 
        return MemberIdDTO.builder()
                .memberId(memberEntity.getMemberId())
                .build();
    }

    @PostMapping("/updateMember")
    public MemberIdDTO updateMember(@RequestBody MemberSaveRequestDTO memberSaveRequestDTO) {
        MemberEntity memberEntity = memberService.updateMember(memberSaveRequestDTO);
        return MemberIdDTO.builder()
                .memberId(memberEntity.getMemberId())
                .build();
    }
    
    @PostMapping("/deleteMember")
    public MemberIdDTO deleteMember(@RequestBody MemberDeleteRequestDTO memberDeleteRequestDTO) {
        String memberId = memberDeleteRequestDTO.getMemberId();
        MemberEntity memberEntity = memberService.deleteMember(memberId);
        return MemberIdDTO.builder()
                .memberId(memberEntity.getMemberId())
                .build();
    }

}
