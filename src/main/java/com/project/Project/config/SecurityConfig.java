package com.project.Project.config;

import com.project.Project.auth.filter.CustomBasicAuthFilter;
import com.project.Project.auth.filter.JwtAuthFilter;
import com.project.Project.auth.handler.*;
import com.project.Project.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.project.Project.auth.service.TokenService;
import com.project.Project.config.properties.SecurityProperties;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@RequiredArgsConstructor
@Configuration
//@PropertySource("application-security.yml")
public class SecurityConfig {

    private final MemberService memberService;
    private final BasicAuthFailureHandler basicAuthFailureHandler;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;
    private final SecurityProperties securityProperties;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    JwtAuthFilter jwtAuthFilter() throws Exception {
        return new JwtAuthFilter(authenticationManager(authenticationConfiguration), new JWTFailureHandler());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    CustomBasicAuthFilter customBasicAuthFilter() throws Exception {
        return new CustomBasicAuthFilter(authenticationManager(authenticationConfiguration), customAuthenticationEntryPoint(), memberService, basicAuthFailureHandler);
    }

    @Bean
    OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenService, memberRepository, oAuth2AuthorizationRequestBasedOnCookieRepository(), securityProperties);
    }

    @Bean
    OAuth2FailureHandler oAuth2FailureHandler() {
        return new OAuth2FailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository(), securityProperties);
    }

    @Bean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    /*
     * 쿠키 기반 인가 Repository
     * 인가 응답을 연계 하고 검증할 때 사용.
     * */
    @Bean
    OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

}