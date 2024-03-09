package org.nikitactr.bookservice.jwt;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            String jwt = parseJwt(req);
            log.error("AuthTokenFilter | doFilterInternal | jwt: {}", jwt);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                for (String rolename : jwtUtils.getRoleNamesFromJwtToken(jwt)) {
                    authorities.add(new SimpleGrantedAuthority(rolename));
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("AuthTokenFilter | doFilterInternal | Cannot set user authentication: {}", e.getMessage());
        }
        chain.doFilter(req, res);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        log.info("AuthTokenFilter | parseJwt | headerAuth: {}", headerAuth);
        if (StringUtils.hasText(headerAuth)) {
            log.info("AuthTokenFilter | parseJwt | parseJwt: {}", headerAuth.substring(7, headerAuth.length()));
            return headerAuth.substring(7);
        }
        return null;
    }
}
