package jpa;


import com.project.Project.ProjectApplication;
import com.project.Project.domain.member.Member;
import com.project.Project.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@SpringBootTest(classes = ProjectApplication.class)
@ActiveProfiles("localProd")
@Rollback(false) // 직접 디비를 변경함
public class MemberNickNameGenerator {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WebClient nickNameWebClient;

    @Test
    @Transactional
    public void updateNickNames() {
        // 테스트를 위해 닉네임이 NULL인 멤버를 가져옵니다.
        List<Member> members = memberRepository.findByNickNameIsNull();

        // 각 멤버의 닉네임을 업데이트 합니다.
        for (int i = 0; i < members.size(); i++) {
            String nickName = nickNameWebClient.get()
                    .uri(uriBuilder -> uriBuilder.queryParam("format", "text").queryParam("max_length", 8).build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            Member member = members.get(i);
            member.setNickName(nickName);
            memberRepository.save(member);
        }

        // 멤버들의 닉네임이 제대로 업데이트 되었는지 확인합니다.
        members = memberRepository.findByNickNameIsNull();
        assert members.isEmpty();
    }
}