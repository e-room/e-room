package com.project.Project.config;

import com.project.Project.auth.filter.JwtAuthFilter;
import com.project.Project.auth.handler.*;
import com.project.Project.auth.provider.JwtProvider;
import com.project.Project.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.project.Project.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.PostConstruct;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtProvider jwtProvider;
    //    private final JwtAuthFilter jwtAuthFilter;
//    private final CustomBasicAuthFilter customBasicAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomLogoutHandler customLogoutHandler;
//    private final JwtExceptionInterceptorFilter jwtExceptionInterceptorFilter;

    private final SecurityConfig securityConfig;

    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private static CustomOAuth2UserService staticCustomOAuth2UserService;
    private static JwtProvider staticJwtProvider;
    //    private static JwtAuthFilter staticJwtAuthFilter;
//    private static CustomBasicAuthFilter staticCustomBasicAuthFilter;
    private static OAuth2SuccessHandler staticOAuth2SuccessHandler;
    private static OAuth2FailureHandler staticOAuth2FailureHandler;
    private static OAuth2AuthorizationRequestBasedOnCookieRepository staticOAuth2AuthorizationRequestBasedOnCookieRepository;
    private static CustomAuthenticationEntryPoint staticCustomAuthenticationEntryPoint;
    private static CustomLogoutHandler staticCustomLogoutHandler;
    private static CustomLogoutSuccessHandler staticCustomLogoutSuccessHandler;
    //    private static JwtExceptionInterceptorFilter staticJwtExceptionInterceptorFilter;
    private static SecurityConfig staticSecurityConfig;

    @PostConstruct
    public void init() {
        staticCustomOAuth2UserService = this.customOAuth2UserService;
        staticJwtProvider = this.jwtProvider;
//        staticJwtAuthFilter = this.jwtAuthFilter;
//        staticCustomBasicAuthFilter = this.customBasicAuthFilter;
        staticOAuth2SuccessHandler = this.oAuth2SuccessHandler;
        staticOAuth2FailureHandler = this.oAuth2FailureHandler;
        staticOAuth2AuthorizationRequestBasedOnCookieRepository = this.oAuth2AuthorizationRequestBasedOnCookieRepository;
        staticCustomAuthenticationEntryPoint = this.customAuthenticationEntryPoint;
        staticCustomLogoutHandler = this.customLogoutHandler;
//        staticJwtExceptionInterceptorFilter = this.jwtExceptionInterceptorFilter;
        staticCustomLogoutSuccessHandler = this.customLogoutSuccessHandler;
        staticSecurityConfig = this.securityConfig;
    }

    @Profile("local")
    @EnableWebSecurity
    public static class SecurityLocalConfig {

        @Bean
        public WebSecurityCustomizer configure() {
            return (web) -> web.ignoring().mvcMatchers(
                    "/token/valid", "api/profile", "/", "/health",
                    "/swagger-ui.html",
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-ui/**"
            );
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return defaultAuthentication()
                    .andThen(oAuthAndJwtAuthentication())
                    .andThen(headerMockingAuthentication())
                    .andThen(exceptionHandler())
                    .andThen(permitAllList())
                    .andThen(defaultAuthorization())
                    .apply(http);
        }

    }

    @Profile("dev")
    @EnableWebSecurity
    public static class SecurityDevConfig {
        @Bean
        public WebSecurityCustomizer configure() {
            return (web) -> web.ignoring().mvcMatchers(
                    "/token/valid", "api/profile", "/", "/health",
                    "/swagger-ui.html",
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/admin/**",
                    "/css/**", "/img/**", "/js/**", "/webjars/**", "/favicon.ico",
                    "/building/room/review/bests"
            );
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return defaultAuthentication()
                    .andThen(oAuthAndJwtAuthentication())
                    .andThen(headerMockingAuthentication())
                    .andThen(exceptionHandler())
                    .andThen(permitAllList())
                    .andThen(defaultAuthorization())
                    .apply(http);
        }
    }

    @Profile("localDev")
    @EnableWebSecurity
    public static class SecurityLocalDevConfig {
        @Bean
        public WebSecurityCustomizer configure() {
            return (web) -> web.ignoring().mvcMatchers(
                    "/token/valid", "api/profile", "/", "/health",
                    "/swagger-ui.html",
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/building/room/review/bests",
                    "/admin/**",
                    "/css/**", "/img/**", "/js/**", "/webjars/**", "/favicon.ico"
            );
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return defaultAuthentication()
                    .andThen(oAuthAndJwtAuthentication())
                    .andThen(headerMockingAuthentication())
                    .andThen(exceptionHandler())
                    .andThen(permitAllList())
                    .andThen(defaultAuthorization())
                    .apply(http);
        }
    }

    @Profile("prod")
    @EnableWebSecurity
    public static class SecurityProdConfig {
        @Bean
        public WebSecurityCustomizer configure() {
            return (web) -> web.ignoring().mvcMatchers(
                    "/token/valid", "api/profile", "/", "/health",
                    "/swagger-ui.html",
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/building/room/review/bests"
            );
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return defaultAuthentication()
                    .andThen(oAuthAndJwtAuthentication())
                    .andThen(exceptionHandler())
                    .andThen(permitAllList())
                    .andThen(defaultAuthorization())
                    .apply(http);
        }
    }

    @Profile("localProd")
    @EnableWebSecurity
    public static class SecurityLocalProdConfig {
        @Bean
        public WebSecurityCustomizer configure() {
            return (web) -> web.ignoring().mvcMatchers(
                    "/token/valid", "api/profile", "/", "/health",
                    "/swagger-ui.html",
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/building/room/review/bests"
            );
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return defaultAuthentication()
                    .andThen(oAuthAndJwtAuthentication())
                    .andThen(headerMockingAuthentication())
                    .andThen(exceptionHandler())
                    .andThen(permitAllList())
                    .andThen(defaultAuthorization())
                    .apply(http);
        }
    }

    private static Function<HttpSecurity, SecurityFilterChain> defaultAuthorization() {
        return (http) -> {
            try {
                http
                        .authorizeRequests()
                        .anyRequest().authenticated();
                return http.build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Function<HttpSecurity, HttpSecurity> permitAllList() throws Exception {
        return (http) -> {
            try {
                http
                        .authorizeRequests()
                        .antMatchers("/login", "api/profile", "/", "/health").permitAll()
                        .antMatchers(HttpMethod.GET, "/building/marking/**", "/building/search", "/building/{buildingId}/images", "/building/{buildingId}", "/building", "/building/{buildingId}/room/review").permitAll()
                        .antMatchers(HttpMethod.GET, "/token/valid").permitAll();
                return http;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Function<HttpSecurity, HttpSecurity> exceptionHandler() throws Exception {
        return (http) -> {
            try {
                http
                        .exceptionHandling()
                        .authenticationEntryPoint(staticCustomAuthenticationEntryPoint);
                return http;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Function<HttpSecurity, HttpSecurity> oAuthAndJwtAuthentication() throws Exception {
        return (http) -> {
            try {
                http
                        .cors()
                        .and()
                        .csrf().disable()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .logout()
                        .logoutUrl("/logout")
                        .addLogoutHandler(staticCustomLogoutHandler)
                        .logoutSuccessHandler(staticCustomLogoutSuccessHandler)
                        .and()
                        .oauth2Login()
                        .authorizationEndpoint()
                        .authorizationRequestRepository(staticOAuth2AuthorizationRequestBasedOnCookieRepository)
                        .and()
//                        .loginPage("/login")
                        .successHandler(staticOAuth2SuccessHandler) // 성공시
                        .failureHandler(staticOAuth2FailureHandler)
                        .userInfoEndpoint().userService(staticCustomOAuth2UserService);

                // 인증을 처리하는 기본필터 대신 별도의 인증로직을 가진 JwtAuthFilter 추가
                // 가능하다면 JwtLoginConfigurer를 만들어보는 것도 좋을 듯
                http.addFilterAt(staticSecurityConfig.jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
                http.addFilterBefore(staticSecurityConfig.jwtExceptionInterceptorFilter(), JwtAuthFilter.class);
                http.authenticationProvider(staticJwtProvider);
                http.httpBasic().disable();
                return http;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

    }


    private static Function<HttpSecurity, HttpSecurity> defaultAuthentication() throws Exception {
        return (http) -> {
            try {
                http
                        .csrf().disable()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .logout()
                        .logoutUrl("/logout")
                        .addLogoutHandler(staticCustomLogoutHandler)
                        .logoutSuccessHandler(staticCustomLogoutSuccessHandler);
                return http;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Function<HttpSecurity, HttpSecurity> headerMockingAuthentication() throws Exception {
        return (http) -> {
            try {
                http.httpBasic().disable();
                http.addFilterAfter(staticSecurityConfig.customBasicAuthFilter(), UsernamePasswordAuthenticationFilter.class);
                return http;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Function<HttpSecurity, HttpSecurity> noAuthentication() throws Exception {
        return (http) -> {
            try {
                http
                        .csrf().disable()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .authorizeRequests()
                        .antMatchers("/**").permitAll();

                http.addFilterAfter(staticSecurityConfig.customBasicAuthFilter(), UsernamePasswordAuthenticationFilter.class);
                return http;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
