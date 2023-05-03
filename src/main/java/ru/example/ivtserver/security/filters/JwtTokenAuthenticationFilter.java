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


        if (token.isPresent() && tokenProvider.isValidToken(token.get())) {

            log.debug("Токен доступа {}", token.get());

            var claim = tokenProvider.getBody(token.get())
                    .orElseThrow(() -> new JwtException("Неверное тело токена доступа"));

            var user = userDetailsService.loadUserByUsername(claim.getSubject());

            log.info("Пользователь {} подтвержден", user.getUsername());

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword(), user.getAuthorities()));
        }
        chain.doFilter(request, response);
    }
}
