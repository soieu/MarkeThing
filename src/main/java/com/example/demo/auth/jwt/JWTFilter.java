package com.example.demo.auth.jwt;

import com.example.demo.exception.MarkethingException;
import com.example.demo.exception.type.ErrorCode;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        // OAuth2 로그인
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                // System.out.println("====================="+cookie+"=====================");
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                }
            }
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

            if(authorization == null || authorization.isBlank()) {
                chain.doFilter(request, response);
                return;
            }

        }

        try {
            String token = authorizationHeader == null ? authorization : authorizationHeader.split(" ")[1];

            Authentication auth = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // 토큰 소멸 시간 검증
            if (jwtUtil.isExpired(token)) {
                throw new MarkethingException(ErrorCode.TOKEN_EXPIRED);

            } else {
                chain.doFilter(request, response);
            }

        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (MarkethingException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}

