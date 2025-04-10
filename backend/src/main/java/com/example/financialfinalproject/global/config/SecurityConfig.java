package com.example.financialfinalproject.global.config;

import com.example.financialfinalproject.global.jwt.filter.JwtAuthenticationProcessingFilter;
import com.example.financialfinalproject.global.jwt.service.JwtService;
import com.example.financialfinalproject.global.login.filter.CustomUsernamePasswordAuthenticationFilter;
import com.example.financialfinalproject.global.login.handler.LoginFailureHandler;
import com.example.financialfinalproject.global.login.handler.LoginSuccessHandler;
import com.example.financialfinalproject.global.login.service.LoginService;
import com.example.financialfinalproject.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.example.financialfinalproject.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.example.financialfinalproject.global.oauth2.service.CustomOAuth2UserService;
import com.example.financialfinalproject.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


/**
 * 인증은 CustomUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리
 * JwtAuthenticationProcessingFilter는 AccessToken, RefreshToken 재발급
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    private final String[] SWAGGER = {
            "/v3/api-docs",
            "/swagger-resources/**", "/configuration/security", "/webjars/**",
            "/swagger-ui.html", "/swagger/**", "/swagger-ui/**"};


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .formLogin().disable()
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers(SWAGGER).permitAll()
                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll()
                .antMatchers("/api/v1/users/{userId}/role").hasRole("ADMIN")
                .antMatchers("/api/v1/**").authenticated()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .anyRequest().permitAll() // 위의 경로 이외에는 모두 접근 가능
                .and()
                //== 소셜 로그인 설정 ==//
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
                .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
                .userInfoEndpoint().userService(customOAuth2UserService); // customUserService 설정

        // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
        // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
        // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomUsernamePasswordAuthenticationFilter
        http.addFilterAfter(customUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("https://api.cointoz.store"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // 쿠키 포함 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * AuthenticationManager 설정 후 등록
     * PasswordEncoder를 사용하는 AuthenticationProvider 지정 (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용)
     * FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
     * UserDetailsService는 커스텀 LoginService로 등록
     * 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, redisTemplate, objectMapper);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler(objectMapper);
    }

    /**
     * CustomUsernamePasswordAuthenticationFilter 빈 등록
     * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
     * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
     */
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordLoginFilter
                = new CustomUsernamePasswordAuthenticationFilter(objectMapper);
        customUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, userRepository, redisTemplate);
    }
}
