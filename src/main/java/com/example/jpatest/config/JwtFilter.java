package com.example.jpatest.config;

import com.example.jpatest.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("auth : {}", auth);

        if(auth == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = auth.split(" ")[1];
        // auth : Bearer eyJhbGciOiJIUzI1NiJ9.e어쩌구저쩌구 나오는거에서 공백을 기준으로 자르고, index1의 값을 가져오느 ㄴ것.

        log.info("authtoken : {}",token );
        if(JwtUtil.isExpired(token, secretKey)) {
//            log.info("auth true or false : {}", JwtUtil.isExpired(token, secretKey) );
            filterChain.doFilter(request, response);
            return;
        }

//        String username = "test";     // 토큰의 "username"은 "test"
        String username = JwtUtil.getUsername(token, secretKey);        // 사용자가 설정한 이름으로 토큰 설정.
        String role = JwtUtil.getRole(token, secretKey);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)) );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);  // authenticationToken에는 username이 들어옴.
        filterChain.doFilter(request, response);
        // 모든 경로에 대해 인증 설정
    }
}
