package com.project.Project.util.generator.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.Project.auth.service.TokenService;
import com.project.Project.domain.member.Member;
import com.project.Project.unit.repository.RepositoryTestConfig;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.service.member.MemberService;
import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewKeywordService;
import com.project.Project.common.util.component.MemberGen;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EnableJpaAuditing
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MemberGen.class, TokenService.class, ReviewCategoryService.class, ReviewKeywordService.class, MemberService.class}))
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RepositoryTestConfig.class)
@Commit
public class TestMemberGenerator {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberGen memberGen;

    private class Node {
        Integer id;
        String testName;
        String testEmail;

        public Node(String testName, String testEmail) {
            this.testName = testName;
            this.testEmail = testEmail;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    @Test
    public void createTestMember() throws JsonProcessingException {

        final String defaultName = "Test";
        final String defaultEmail = "e-room";
        final String defaultEmailSuffix = "@e-room.app";

        List<Node> testDataList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            String testName = defaultName + "+" + i;
            String testEmail = defaultEmail + "+" + i + defaultEmailSuffix;
            Node testData = new Node(testName, testEmail);
            testDataList.add(testData);
        }
        List<Member> testMembers = testDataList.stream()
                .map((testData) -> memberGen.createMember(testData.testName, testData.testEmail))
                .collect(Collectors.toList());

        String json = new ObjectMapper().registerModule(new JavaTimeModule()).writerWithDefaultPrettyPrinter().writeValueAsString(testMembers);
        String fileName = "members-" + new LocalDate();

        File folder = new File(Paths.get("src/main/resources/testData/members/").toUri());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File memberGenResult = new File(Paths.get("src/main/resources/testData/members/", fileName).toUri());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(memberGenResult, true);
            FileChannel channel = fileOutputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(64);
            buffer.clear();
            buffer.put(json.getBytes());

            buffer.flip();

            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }

            fileOutputStream.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
