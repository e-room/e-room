package com.project.Project.bootstrap;

import com.project.Project.domain.checklist.Question;
import com.project.Project.repository.checklist.QuestionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 서버 기동 시점에
 * DB에 질문이 하나도 없으면
 * 정해진 질문들을 Insert 해주는 클래스
 */
@Component
@RequiredArgsConstructor
public class QuestionGenerator implements CommandLineRunner {

    private final QuestionRepository questionRepository;

    @Getter
    private class DefaultQuestion {
        private String query;
        private String description;
        private String keyword;

        public DefaultQuestion(String query, String description, String keyword) {
            this.query = query;
            this.description = description;
            this.keyword = keyword;
        }
    }

    private final List<DefaultQuestion> defaultQuestionList = List.of(
            new DefaultQuestion("수압은 어떤가요?", "세면대와 싱크대 물을 같이 틀고 변기물도 내려보세요", "수압"),
            new DefaultQuestion("난방은 잘 되나요?", "난방기를 틀어보고 몇 분 후 방 전체의 온도 변화를 확인하세요", "난방"),
            new DefaultQuestion("인터넷 연결은 어떤가요?", "Wi-Fi 또는 유선 인터넷의 연결 상태와 속도를 확인하세요", "인터넷"),
            new DefaultQuestion("창문과 문은 밀폐 잘 되나요?", "창문과 문을 닫아보고 틈새로 바람이 들어오는지 확인하세요", "밀폐"),
            new DefaultQuestion("전기와 전등은 잘 작동하나요?", "모든 스위치와 콘센트를 확인해보세요", "전기")
    );

    @Override
    public void run(String... args) throws Exception {
        long totalCount = questionRepository.count();
        if(totalCount > 0) return ;

        List<Question> questionList = defaultQuestionList.stream()
                .map(defaultQuestion -> Question.builder()
                        .query(defaultQuestion.getQuery())
                        .description(defaultQuestion.getDescription())
                        .keyword(defaultQuestion.getKeyword())
                        .build())
                .collect(Collectors.toList());

        questionRepository.saveAll(questionList);
    }
}
