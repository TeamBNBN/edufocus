package com.edufocus.edufocus.user.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import com.edufocus.edufocus.user.model.exception.ExpriedTokenException;
import com.edufocus.edufocus.user.model.exception.InvalidTokenException;
import com.edufocus.edufocus.user.model.exception.UnAuthorizedException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil {

    @Value("${jwt.salt}")
    private String salt;

    @Value("${jwt.access-token.expiretime}")
    private long accessTokenExpireTime;

    @Value("${jwt.refresh-token.expiretime}")
    private long refreshTokenExpireTime;

    public String createAccessToken(String id) {
        return create(id, "access-token", accessTokenExpireTime);
    }

    public String createRefreshToken(String id) {
        return create(id, "refresh-token", refreshTokenExpireTime);
    }

    private String create(String id, String subject, long expireTime) {
        Claims claims = Jwts.claims()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime));
        claims.put("id", id);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, generateKey())
                .compact();
    }

    private byte[] generateKey() {
        return salt.getBytes(StandardCharsets.UTF_8);
    }

    public boolean checkToken(String token) {
        try {
                Jws<Claims> claims = Jwts.parserBuilder()
                        .setSigningKey(generateKey())
                        .build()
                        .parseClaimsJws(token);
                log.debug("claims: {}", claims);
                return true;
            } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | SignatureException e) {
                log.error("Token validation error: {}", e.getMessage());

                return false;
            }
        catch ( ExpiredJwtException e)
            {
                throw new ExpriedTokenException();

            }
        catch (Exception e) {
                System.out.println(token);
                System.out.println(e.getMessage());
                log.error("Unexpected error while validating token: {}", e.getMessage());
                return false;
        }
    }

    public String getUserId(String authorization) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(authorization);
            Map<String, Object> value = claims.getBody();
            log.info("value : {}", value);
            return (String) value.get("id");
        }catch ( ExpiredJwtException e)
        {
            System.out.println("expired token");
            throw new ExpriedTokenException();

        }
        catch (Exception e) {
            log.error("Failed to get user ID from token: {}", e.getMessage());
            throw new InvalidTokenException();
        }
    }
}
