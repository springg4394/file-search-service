package com.file.search.service.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.file.search.service.model.MemberEntity;
import com.file.search.service.repository.MemberRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MemberEntity> member = memberRepository.findById(username);
        if (!member.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        
        MemberEntity memberEntity = member.get();

        // CustomUserDetails 객체 생성
        return new CustomUserDetails(
            memberEntity.getMemberId(),
            memberEntity.getMemberPassword(),
            memberEntity.getAdminYn()
        );
    }
}
