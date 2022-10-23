package com.project.Project.auth.filter;

import com.project.Project.auth.JwtAuthentication;
import com.project.Project.auth.dto.MemberDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.enums.MemberRole;
import com.project.Project.serializer.member.MemberSerializer;
import com.project.Project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CustomBasicAuthFilter extends OncePerRequestFilter {

    private final MemberService memberService;

    private Member mockMember(Long id) {
        return this.memberService.findById(id).orElseGet(() -> Member.builder() // temp user
                .reviewList(new ArrayList<>())
                .favoriteBuildingList(new ArrayList<>())
                .likeReviewList(new ArrayList<>())
                .name("하품하는 망아지")
                .email("swa07016@khu.ac.kr")
                .memberRole(MemberRole.USER)
                .refreshToken("mockingMember")
                .profileImageUrl("https://lh3.googleusercontent.com/ogw/AOh-ky20QeRrWFPI8l-q3LizWDKqBpsWTIWTcQa_4fh5=s64-c-mo")
                .build()
        );
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String keyId = request.getHeader("mocking");
        Long mockMemberId = Long.parseLong(keyId);
        Member member = this.mockMember(mockMemberId);
        MemberDto memberDto = MemberSerializer.toDto(member);

        Authentication auth = getAuthentication(memberDto, member);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private Authentication getAuthentication(MemberDto memberDto, Member member) {
        return new JwtAuthentication(
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")),
                memberDto, member,
                member.getRefreshToken()
        );
    }
}
