package com.file.search.service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.file.search.service.dto.member.MemberListResponseDTO;
import com.file.search.service.dto.member.MemberSaveRequestDTO;
import com.file.search.service.model.MemberEntity;
import com.file.search.service.repository.MemberRepository;
import com.file.search.service.service.common.SecurityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository      memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecurityService       securityService;

    @Transactional(readOnly = true)
    public List<MemberListResponseDTO> selectMemberList() {
        List<MemberEntity> memberEntities = memberRepository.findAll();

        return memberEntities.stream()
            .map(memberEntity -> {
                MemberListResponseDTO dto = new MemberListResponseDTO();
                dto.setMemberId(memberEntity.getMemberId());
                dto.setMemberName(memberEntity.getMemberName());
                dto.setRegisterId(memberEntity.getRegisterId());
                dto.setRegisterDatetime(memberEntity.getRegisterDatetime());
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public MemberEntity saveMember(MemberSaveRequestDTO memberSaveRequestDTO) {
        String loginId = securityService.getLoginId();
        
        MemberEntity memberEntity = MemberEntity.builder()
            .memberId(memberSaveRequestDTO.getMemberId())
            .memberName(memberSaveRequestDTO.getMemberName())
            .memberPassword(bCryptPasswordEncoder.encode(memberSaveRequestDTO.getMemberPassword()))
            .registerId(loginId)
            .build();

        return memberRepository.save(memberEntity);
    }

    public Optional<MemberEntity> getMember(String memberId) {
        return memberRepository.findById(memberId);
    }

    @Transactional
    public MemberEntity updateMember(MemberSaveRequestDTO memberSaveRequestDTO) {
        String loginId = securityService.getLoginId();
        
        MemberEntity memberEntity = memberRepository.findById(memberSaveRequestDTO.getMemberId())
            .orElseThrow(() -> new RuntimeException("Member not found"));

        String encryptedPassword = Optional.ofNullable(memberSaveRequestDTO.getMemberPassword())
            .map(password -> bCryptPasswordEncoder.encode(password))
            .orElse(memberEntity.getMemberPassword());

        MemberEntity updateMemberEntity = MemberEntity.builder()
            .memberId(memberEntity.getMemberId())
            .memberName(Optional.ofNullable(memberSaveRequestDTO.getMemberName()).orElse(memberEntity.getMemberName()))
            .memberPassword(encryptedPassword)
            .modifyId(loginId)
            .build();

        return memberRepository.save(updateMemberEntity);
    }

    @Transactional
    public MemberEntity deleteMember(String memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
        .orElseThrow(() -> new RuntimeException("Member not found"));

        memberRepository.deleteById(memberEntity.getMemberId());
        return memberEntity;
    }

}
