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
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Конфигурация безопасности для приложения.
 */
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

    /**
     * Создает экземпляр {@link SecurityFilterChain} для настройки безопасности приложения.
     * @param http {@link HttpSecurity} для настройки защиты ресурсов приложения.
     * @return SecurityFilterChain для применения настроек безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .exceptionHandling().authenticationEntryPoint(delegatedEntryPoint)
                .and()
                .addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/login", "/refresh", "/partner",
                                "/recover/pass", "/recover/pass/valid", "/change/email", "/images/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/data", "/teacher/image/*",
                                "/partner/image/*", "/review/image/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/teacher").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors();

        http.csrf().disable().httpBasic().disable();
        return http.build();
    }

    /**
     * Создает экземпляр {@link AuthenticationManager} для аутентификации пользователей.
     * @return AuthenticationManager для аутентификации пользователей.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    /**
     * Создает экземпляр {@link AuthenticationProvider} для аутентификации пользователей.
     * @return AuthenticationProvider для аутентификации пользователей.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    /**
     * Создает экземпляр {@link PasswordEncoder} с использованием {@link BCryptPasswordEncoder}.
     * @return PasswordEncoder для хэширования паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
