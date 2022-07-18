package com.project.Project.config.auth;

import com.project.Project.filter.JwtAuthFilter;
import com.project.Project.service.MemberService;
import com.project.Project.service.impl.CustomOAuth2UserService;
import com.project.Project.service.impl.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenService tokenService;
    private final MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
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
        http.addFilterBefore(new JwtAuthFilter(tokenService, memberService), UsernamePasswordAuthenticationFilter.class);
    }
}