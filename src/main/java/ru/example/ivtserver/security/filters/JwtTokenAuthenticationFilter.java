package ru.example.ivtserver.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.example.ivtserver.entities.dao.auth.MessageErrorDto;
import ru.example.ivtserver.security.token.JwtReusableTokenProvider;

import java.io.IOException;
import java.util.List;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    static List<String> endPoints = List.of("/login", "/refresh");

    JwtReusableTokenProvider tokenProvider;
    UserDetailsService userDetailsService;
    ObjectMapper mapper;

    @Autowired
    public JwtTokenAuthenticationFilter(@Qualifier("jwtAccessTokenProvider") JwtReusableTokenProvider tokenProvider,
                                        UserDetailsService userDetailsService,
                                        ObjectMapper mapper) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        var token = tokenProvider.getToken(request);

        if (token.isPresent()) {

            log.info("Токен пользователя {}", token);

            var claim = tokenProvider.getBody(token.get());

            if (claim.isPresent()) {
                var user = userDetailsService.loadUserByUsername(claim.get().getSubject());

                if (log.isDebugEnabled()) {
                    log.debug("Пользователь {}", user.getUsername());
                }

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword(), user.getAuthorities()));
                chain.doFilter(request, response);
            } else {
                sendError(request, response);
            }
        } else {
            sendError(request, response);
        }
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(401);

        var messageBody = MessageErrorDto.builder()
                .message("Неверный формат токена")
                .status(401)
                .path(request.getContextPath() + request.getServletPath())
                .build();

        mapper.writeValue(response.getOutputStream(), messageBody);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        for (var endPoint : endPoints) {
            if (endPoint.equals(request.getServletPath())) {
                return true;
            }
        }
        return false;
    }
}
