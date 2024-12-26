package com.file.search.service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.search.service.security.JWTUtil;
import com.file.search.service.service.common.RedisService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ReissueController {

    private final Long ACCESS_TOKEN_EXPIRE = 600000L; //10분
    private final Long REFRESH_TOKEN_EXPIRE = 86400000L; //24시간

    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.UNAUTHORIZED);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.UNAUTHORIZED);
        }

        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String existsRefresh = redisService.getRefreshToken(username);
        if (existsRefresh == null) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        // 새로운 토큰 발급
        String newAccess = jwtUtil.createJwt("access", username, role, ACCESS_TOKEN_EXPIRE);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, REFRESH_TOKEN_EXPIRE);

        // 기존 토큰 삭제 후 새로운 토큰 저장
        redisService.deleteRefreshToken(username);
        redisService.saveRefreshToken(username, newRefresh, REFRESH_TOKEN_EXPIRE);

        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}
