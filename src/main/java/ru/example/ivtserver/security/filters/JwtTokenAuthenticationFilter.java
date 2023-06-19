package ru.example.ivtserver.security.filters;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.example.ivtserver.security.token.JwtAuthenticationTokenProvider;

import java.io.IOException;


/**
 * Фильтр для защищенных конечных точек Spring Security, который осуществляет аутентификацию пользователя на основе JWT-токена.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    JwtAuthenticationTokenProvider tokenProvider;
    UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenAuthenticationFilter(@Qualifier("jwtAccessTokenProvider") JwtAuthenticationTokenProvider tokenProvider,
                                        UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        var token = tokenProvider.getToken(request);

        token.ifPresent(t -> {
            if (tokenProvider.isValidToken(t)) {
                log.debug("Токен доступа {}", t);

                var claim = tokenProvider.getBody(t)
                        .orElseThrow(() -> new JwtException("Неверное тело токена доступа"));

                var user = userDetailsService.loadUserByUsername(claim.getSubject());

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword(), user.getAuthorities()));
            }
        });
        chain.doFilter(request, response);
    }
}
