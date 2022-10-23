package com.project.Project.auth;

import com.project.Project.auth.dto.MemberDto;
import com.project.Project.domain.Member;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class JwtAuthentication extends AbstractAuthenticationToken {

    private final MemberDto principal;
    private final String refreshToken;

    public JwtAuthentication(Collection<? extends GrantedAuthority> authorities, MemberDto principal, Member details, String refreshToken) {
        super(authorities);
        setAuthenticated(true);
        this.refreshToken = refreshToken;
        this.principal = principal;
        setDetails(details);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
