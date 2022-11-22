package com.project.Project.util.annotation;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithMockCustomOAuth2AccountSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomOAuth2Account> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomOAuth2Account customOAuth2Account) {

        // 1
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // 2
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", customOAuth2Account.name());
        attributes.put("email", customOAuth2Account.email());
        attributes.put("picture", customOAuth2Account.picture());

        // 3
        OAuth2User principal = new DefaultOAuth2User(
                List.of(new OAuth2UserAuthority(customOAuth2Account.role(), attributes)),
                attributes,
                customOAuth2Account.name());

        // 4
        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
                principal,
                principal.getAuthorities(),
                customOAuth2Account.registrationId());

        // 5
        context.setAuthentication(token);
        return context;
    }
}
