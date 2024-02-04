package com.customer.shubham.serviceimpl;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.customer.shubham.dto.TokenRequestDto;
import com.google.common.collect.Maps;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class SecurityServiceImpl {
    String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    private static final String JWT_TOKEN_ISSUER = "Shubham";
    private static final String AUTHORIZATION_SUBJECT = "Authorization Token";
    private String springPassword;


    public static  SecretKeySpec secretKeySpec;

    @Autowired
    public SecurityServiceImpl(@Value("${spring.security.user.password}") final String springPassword) {
        this.springPassword = springPassword;
        this.secretKeySpec = new SecretKeySpec(springPassword.getBytes(StandardCharsets.UTF_8), signatureAlgorithm.getJcaName());
    }


    public String generateJwtToken(final TokenRequestDto requestDto, final Map<String, Object> payload) {
        try {
            if(Strings.isBlank(requestDto.getUsername())){
                throw new Error("User name is manderoty");
            }
//            return AUTHORIZATION_TOKEN_PREFIX.concat(Jwts
//                    .builder()
//                    .setHeader(getJwtHeader(signatureAlgorithm.getValue()))
//                    .setId(UUID.randomUUID().toString())
//                    .setSubject(AUTHORIZATION_SUBJECT)
//                    .setIssuedAt(new Date())
//                    .setIssuer(JWT_TOKEN_ISSUER)
//                    .setAudience(getEncryptedUserName(requestDto))
//                    .setExpiration(getJwtExpiryDate())
//                    .addClaims(getPayload(payload))
//                    .signWith(signatureAlgorithm, secretKeySpec)
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

    private Map<String, Object> getJwtHeader(final String algorithm) {
        final HashMap<String, Object> headers = Maps.newHashMap();
        headers.put("typ", "JWT");
        headers.put("alg", algorithm);
        return headers;
    }

    private Date getJwtExpiryDate() {
        return LocalDateTime.now().plusMinutes(5).toDate();
    }

    private String getEncryptedUserName(final TokenRequestDto requestDto) {
        try {
            final Cipher aesGsm = Cipher.getInstance("AES/GCM/NoPadding");
            final SecretKeySpec secretKeySpecification = new SecretKeySpec(Arrays.copyOf(requestDto.getPassword().getBytes(StandardCharsets.UTF_8), 32), "AES");
            final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, new byte[12]);
            aesGsm.init(Cipher.ENCRYPT_MODE, secretKeySpecification, gcmParameterSpec);
            return Base64.getEncoder().encodeToString(aesGsm.doFinal(requestDto.getUsername().getBytes(StandardCharsets.UTF_8)));
        } catch (final Exception e) {
            throw new Error("Unable to perform AES encryption");
        }
    }

    private Map<String, Object> getPayload(final Map<String, Object> payload) {
        if (Objects.isNull(payload)) {
            return Maps.newHashMap();
        }
        return payload;
    }

    public String getAuthorizationHeader(TokenRequestDto requestDto) {
        try {
            return generateJwtToken(requestDto, Maps.newHashMap());
        } catch (final Exception e) {
            return "";
        }
    }

   public Boolean validateToken(String token, UserDetails user){
     String username=validaUserTokenToken(token);
     return (user.getUsername().equals(username));

   }

    public String validaUserTokenToken(final String jwtToken) {
        final Claims claims = extractClaims(jwtToken);
        if (StringUtils.isBlank(claims.getAudience())) {
            throw new RuntimeException("Claim does not contains audience");
        }
        return getDecryptedUserName(claims.getAudience());
    }

    private Claims extractClaims(final String jwtToken) {
        final JwtParser jwtParser = Jwts.parser();
        if (!jwtParser.isSigned(jwtToken)) {
            throw new RuntimeException("Unsigned token found, signature is mandatory");
        }
        try {
            final Jws<Claims> jwtInFormOfJws = jwtParser.setSigningKey(secretKeySpec).parseClaimsJws(jwtToken);
            final Claims claims = jwtInFormOfJws.getBody();
            if (Objects.nonNull(claims)) {
                return claims;
            }
            return Jwts.claims();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Authorization token expired");
        }
    }

    private String getDecryptedUserName(final String encryptedUserName) {
        try {
            final Cipher aesGsm = Cipher.getInstance("AES/GCM/NoPadding");
            final SecretKeySpec secretKeySpecification = new SecretKeySpec(Arrays.copyOf(springPassword.getBytes(StandardCharsets.UTF_8), 32), "AES");
            final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, new byte[12]);
            aesGsm.init(Cipher.DECRYPT_MODE, secretKeySpecification, gcmParameterSpec);
            return new String(aesGsm.doFinal(Base64.getDecoder().decode(encryptedUserName.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        } catch (final Exception e) {
            throw new RuntimeException("Malformed authorization token");
        }
    }
}




