package com.file.search.service.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.search.service.dto.auth.LoginRequestDTO;
import com.file.search.service.dto.auth.LoginResponseDTO;
import com.file.search.service.model.MemberEntity;
import com.file.search.service.security.CustomUserDetails;
import com.file.search.service.security.JWTUtil;
import com.file.search.service.service.MemberService;
import com.file.search.service.service.common.RedisService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final Long ACCESS_TOKEN_EXPIRE = 600000L; //10분
    private final Long REFRESH_TOKEN_EXPIRE = 86400000L; //24시간

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        if (loginRequestDTO.getMemberId() == null || loginRequestDTO.getMemberPassword() == null) {
            loginResponseDTO.setResult(false);
            loginResponseDTO.setMessage("아이디와 비밀번호는 필수로 입력해야 합니다.");
            return ResponseEntity.badRequest().body(loginResponseDTO);
        }
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequestDTO.getMemberId(), 
                    loginRequestDTO.getMemberPassword(), 
                    null
                )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            String userId = userDetails.getUsername();
            String userRole = userDetails.getAuthorities().toArray()[0].toString();

            // 토큰 생성
            String access = jwtUtil.createJwt("access", userId, userRole, ACCESS_TOKEN_EXPIRE);
            String refresh = jwtUtil.createJwt("refresh", userId, userRole, REFRESH_TOKEN_EXPIRE);

            // 사용자 존재 확인
            Optional<MemberEntity> memberEntity = memberService.getMember(userId);
            if (memberEntity.isPresent()) {
                // refresh 토큰 redis에 저장
                redisService.saveRefreshToken(loginRequestDTO.getMemberId(), refresh, REFRESH_TOKEN_EXPIRE);

                // refresh 토큰 쿠키로 응답
                response.addCookie(createCookie("refresh", refresh));

                // 사용자 정보 저장
                MemberEntity member = memberEntity.get();
                member.setMemberPassword(null);

                loginResponseDTO.setResult(true);
                loginResponseDTO.setMemberInfo(member);
                loginResponseDTO.setMessage("로그인 성공");

                return ResponseEntity.ok()
                        .header("Authorization", "Bearer " + access)
                        .body(loginResponseDTO);
            } else {
                //회원 ID가 존재하지 않는 경우
                loginResponseDTO.setResult(false);
                loginResponseDTO.setMessage("아이디와 비밀번호는 필수로 입력해야 합니다.");
                return ResponseEntity.status(401).body(loginResponseDTO);
            }

        } catch (BadCredentialsException e) {
            //비밀번호가 일치하지 않는 경우
            loginResponseDTO.setResult(false);
            loginResponseDTO.setMessage("아이디 또는 비밀번호가 잘못 되었습니다.");
            return ResponseEntity.status(401).body(loginResponseDTO);
        } catch (Exception e) {
            // 기타 예외 처리
            loginResponseDTO.setResult(false);
            loginResponseDTO.setMessage("Internal server error.");
            return ResponseEntity.status(500).body(loginResponseDTO);
        }
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

}
