package com.project.Project.config;

import com.project.Project.auth.CustomAuthenticationEntryPoint;
import com.project.Project.auth.filter.CustomBasicAuthFilter;
import com.project.Project.auth.filter.JwtAuthFilter;
import com.project.Project.auth.handler.BasicAuthFailureHandler;
import com.project.Project.auth.handler.JWTFailureHandler;
import com.project.Project.auth.handler.OAuth2SuccessHandler;
import com.project.Project.auth.provider.JwtProvider;
import com.project.Project.auth.service.CustomOAuth2UserService;
import com.project.Project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@PropertySource("application-security.yml")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final BasicAuthFailureHandler basicAuthFailureHandler;


    @Value("${security.profiles.active}")
    private String stage;

    @Bean
    JwtAuthFilter jwtAuthFilter() throws Exception {
        return new JwtAuthFilter(super.authenticationManager(), new JWTFailureHandler());
    }

    @Bean
    CustomBasicAuthFilter customBasicAuthFilter() throws Exception {
        return new CustomBasicAuthFilter(super.authenticationManager(), new CustomAuthenticationEntryPoint(), memberService, basicAuthFailureHandler);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (stage.equals("dev")) {
            devSetting(http);
        } else if (stage.equals("unit-test")) {
            unitTestSetting(http);
        } else if (stage.equals("integration-test")) {
            integrationTestSetting(http);
        }
    }

    private void integrationTestSetting(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/token/**", "/login/**", "api/profile", "/", "/health").permitAll()
                .antMatchers("/building/marking").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutSuccessUrl("/login")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .successHandler(oAuth2SuccessHandler) // 성공시
                .userInfoEndpoint().userService(customOAuth2UserService);

        // 인증을 처리하는 기본필터 대신 별도의 인증로직을 가진 JwtAuthFilter 추가
        // 가능하다면 JwtLoginConfigurer를 만들어보는 것도 좋을 듯
        http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(jwtProvider);
        http.httpBasic().disable();
        http.addFilterAfter(customBasicAuthFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private void unitTestSetting(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/token/**", "/login/**", "api/profile", "/", "/health").permitAll()
                .antMatchers("/building/marking").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutSuccessUrl("/login")
                .and();
        http.httpBasic().disable();
        http.addFilterAfter(customBasicAuthFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private void devSetting(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll();
        http.addFilterAfter(customBasicAuthFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}