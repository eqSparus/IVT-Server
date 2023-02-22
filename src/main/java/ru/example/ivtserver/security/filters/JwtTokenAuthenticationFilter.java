package ru.example.ivtserver.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.example.ivtserver.security.token.TokenProvider;

import java.io.IOException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Log4j2
public class JwtTokenAuthenticationFilter extends GenericFilterBean {

    TokenProvider tokenProvider;
    UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenAuthenticationFilter(@Qualifier("jwtAccessTokenProvider") TokenProvider tokenProvider,
                                        UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        var request = (HttpServletRequest) servletRequest;

        var token = tokenProvider.getToken(request);

        token.ifPresent(t -> {
            log.info("Токен пользователя {}", t);

            if (tokenProvider.isValidToken(t)) {
                var user = userDetailsService
                        .loadUserByUsername(tokenProvider.getEmailFromToken(t));

                log.info("Пользователь {}", user);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword(), user.getAuthorities()));
            }
        });
        chain.doFilter(servletRequest, servletResponse);

    }
}
