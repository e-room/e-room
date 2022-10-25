package com.project.Project.config;

import com.project.Project.auth.filter.CustomBasicAuthFilter;
import com.project.Project.auth.filter.JwtAuthFilter;
import com.project.Project.auth.handler.OAuth2SuccessHandler;
import com.project.Project.auth.provider.JwtProvider;
import com.project.Project.auth.service.CustomOAuth2UserService;
import com.project.Project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Value("${spring.profiles.active}")
    private String env;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/token/**").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .loginPage("/token/expired")
                .successHandler(oAuth2SuccessHandler) // 성공시
                .userInfoEndpoint().userService(customOAuth2UserService);

        // 인증을 처리하는 기본필터 대신 별도의 인증로직을 가진 JwtAuthFilter 추가
        // 가능하다면 JwtLoginConfigurer를 만들어보는 것도 좋을 듯
        http.addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(jwtProvider);
        if (env.equals("local")) {
            new OAuth2LoginConfigurer<>();
            http.addFilterAt(new CustomBasicAuthFilter(memberService), BasicAuthenticationFilter.class);
        } else {
            http.httpBasic().disable();
        }
    }
}