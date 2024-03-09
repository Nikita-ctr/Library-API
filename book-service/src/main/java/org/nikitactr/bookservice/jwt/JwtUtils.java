package org.nikitactr.bookservice.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JwtUtils extends SecurityProperties.Filter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String getUserNameFromJwtToken(String token) {
        if(token.startsWith("Bearer")){
            token = token.substring(7);
        }
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
    public String[] getRoleNamesFromJwtToken(String token) {
        if(token.startsWith("Bearer")){
            token = token.substring(7);
        }
        String username=Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getIssuer();
        return  username.split(" ");
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("JwtUtils | validateJwtToken | Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JwtUtils | validateJwtToken | JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JwtUtils | validateJwtToken | JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JwtUtils | validateJwtToken | JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
