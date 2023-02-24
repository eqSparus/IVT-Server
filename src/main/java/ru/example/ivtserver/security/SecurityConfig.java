package ru.example.ivtserver.security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    UserDetailsService userDetailsService;
    AuthenticationEntryPoint delegatedEntryPoint;
    OncePerRequestFilter jwtTokenAuthenticationFilter;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          @Qualifier("appAuthenticationEntryPoint") AuthenticationEntryPoint delegatedEntryPoint,
                          @Qualifier("jwtTokenAuthenticationFilter") OncePerRequestFilter jwtTokenAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.delegatedEntryPoint = delegatedEntryPoint;
        this.jwtTokenAuthenticationFilter = jwtTokenAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .exceptionHandling().authenticationEntryPoint(delegatedEntryPoint)
                .and()
                .addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(HttpMethod.POST, "/login", "/refresh").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().configurationSource(corsConfigurationSource());

        http.csrf().disable().httpBasic().disable();

        return http.build();
    }

    @Bean
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new ExceptionHandlerExceptionResolver();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var cordConfig = new CorsConfiguration();
        var source = new UrlBasedCorsConfigurationSource();

        cordConfig.setAllowedOrigins(List.of("https://"));
        cordConfig.setAllowedMethods(List.of(HttpMethod.GET.name(), HttpMethod.POST.name(),
                HttpMethod.DELETE.name(), HttpMethod.PUT.name(), HttpMethod.HEAD.name()));

        source.registerCorsConfiguration("/**", cordConfig);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
