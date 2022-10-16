package com.budgets.modules.security.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RequiredArgsConstructor
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        request.setAttribute("Authorization", "");
        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer ")){
            UsernamePasswordAuthenticationToken authentication = getAuthentication(authorization);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String authorization) throws JsonProcessingException {
        String token = authorization.substring(7);
        if (jwtUtil.isTokenValid(token)){
            Long id = jwtUtil.getId(token);
            return new UsernamePasswordAuthenticationToken(id, null, null);
        }
        throw new UsernameNotFoundException("Token Invalid");
    }
}
