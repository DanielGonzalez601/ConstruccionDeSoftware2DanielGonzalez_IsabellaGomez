package com.bank.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bank.application.port.output.UserRepositoryPort;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
* CONFIG - JwtAuthenticationFilter
*
* Intercepta cada solicitud, extrae el token JWT del encabezado Authorization,
* lo valida, carga la entidad completa del dominio User y la establece como el
* principal de Spring Security. Esto permite que getCurrentUser() funcione en los casos de uso.
*/
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepositoryPort userRepository;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserRepositoryPort userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String identificationNumber = jwtTokenProvider.getIdentificationNumberFromToken(token);

            userRepository.findByIdentificationNumber(identificationNumber).ifPresent(user -> {
                var auth = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            });
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
