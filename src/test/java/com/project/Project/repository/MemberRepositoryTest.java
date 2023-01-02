package com.project.Project.repository;

import com.project.Project.domain.member.Member;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewKeywordService;
import com.project.Project.unit.repository.RepositoryTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewKeywordService.class, ReviewCategoryService.class}))
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RepositoryTestConfig.class)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    Member savedMember;
    @Test
    void delete_Test() {
        // given : 멤버 한명 생성
        Member member = Member.builder()
                .reviewList(new ArrayList<>())
                .favoriteBuildingList(new ArrayList<>())
                .reviewLikeList(new ArrayList<>())
                .build();
        savedMember = memberRepository.save(member);
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.isDeleted()).isFalse();

        // when : memberRepository의 deleteById 메소드를 통해 deleted 플래그를 true로 세팅 (soft delete)
        memberRepository.deleteById(savedMember.getId());

        /*
            then :
            findById를 사용하면 Member 엔티티의 @Where(clause = "deleted=false")로 인해 삭제한 유저를 조회할 수 없음.
            대신, 네이티브 쿼리를 사용하여 구현한 findAllDeletedMember 메소드로 조회
        */
        List<Member> deletedMemberList = memberRepository.findAllDeletedMember();
        assertThat(deletedMemberList.size()).isEqualTo(1);

        Member deletedMember = deletedMemberList.get(0);
        assertThat(deletedMember.isDeleted()).isTrue();
    }
}
