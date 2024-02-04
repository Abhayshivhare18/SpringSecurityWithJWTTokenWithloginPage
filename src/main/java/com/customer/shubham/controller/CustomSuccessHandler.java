package com.customer.shubham.controller;

import com.customer.shubham.dto.TokenRequestDto;
import com.customer.shubham.serviceimpl.SecurityServiceImpl;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.util.Strings;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class CustomSuccessHandler implements AuthenticationSuccessHandler {





    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Map<String, String> map = new HashMap<>();
        TokenRequestDto  requestDto = new TokenRequestDto();
        requestDto.setUsername(username);
        String token = generateJwtToken(requestDto, Maps.newHashMap());
        request.getSession().setAttribute("token",token);
        response.sendRedirect("/dashBoard");
    }


    public String generateJwtToken(final TokenRequestDto requestDto, final Map<String, Object> payload) {
        try {
            if(Strings.isBlank(requestDto.getUsername())){
                throw new Error("User name is manderoty");
            }
//            return SecurityServiceImpl.AUTHORIZATION_TOKEN_PREFIX.concat(Jwts
//                    .builder()
//                    .setHeader(SecurityServiceImpl.getJwtHeader(SecurityServiceImpl.signatureAlgorithm.getValue()))
//                    .setId(UUID.randomUUID().toString())
//                    .setSubject(SecurityServiceImpl.AUTHORIZATION_SUBJECT)
//                    .setIssuedAt(new Date())
//                    .setIssuer(SecurityServiceImpl.JWT_TOKEN_ISSUER)
//                    .setAudience(SecurityServiceImpl.getEncryptedUserName(requestDto))
//                    .setExpiration(SecurityServiceImpl.getJwtExpiryDate())
//                    .addClaims(SecurityServiceImpl.getPayload(payload))
//                    .signWith(SecurityServiceImpl.signatureAlgorithm, SecurityServiceImpl.secretKeySpec)
//                    .compact());

            return Jwts.builder()
                    .setSubject(requestDto.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(LocalDateTime.now().plusMinutes(5).toDate())
                    .signWith(SignatureAlgorithm.HS512, SecurityServiceImpl.secretKeySpec)
                    .compact();
        }catch (final Exception e) {
            throw new Error("Unable to generate token"+ e.getMessage());
        }
    }
}
