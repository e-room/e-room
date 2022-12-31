package JPA;

import com.project.Project.ProjectApplication;
import com.project.Project.user.entity.UserEntity;
import com.project.Project.user.repository.UserRepository;
import com.project.Project.user.repository.UserRepositoryImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import static org.assertj.core.api.Assertions.assertThat;


@Log4j2
@SpringBootTest(classes = ProjectApplication.class)
public class FlushTest {

    @PersistenceUnit
    private EntityManagerFactory factory;

    @Autowired
    UserRepositoryImpl queryDsl;

    @Autowired
    UserRepository userRepository;

    private UserEntity savedMember;

    @BeforeEach
    private void beforeEach() {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        UserEntity member = new UserEntity();
        member.setName("Larry");
        member.setAge(10);
        log.info("----------------before before each-------------");
        em.persist(member);
        savedMember = member;
        em.flush();
        em.getTransaction().commit();
        em.close();
        log.info("-----------------after before each--------");
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void queryDslEvent() {
        log.info("repository 쿼리 수행 전입니다.");
        UserEntity member1 = userRepository.findById(savedMember.getId()).get();
        log.info("repository 쿼리 수행 후입니다.");

        log.info("queryDsl update 쿼리 수행 전입니다.");
        queryDsl.updateUser(member1, 20);
        log.info("queryDsl update 쿼리 수행 후입니다.");
        log.info("queryDsl select 쿼리 수행 전입니다.");
        UserEntity selectedUser = queryDsl.selectUser(member1.getId());
        assertThat(selectedUser.getAge()).isEqualTo(20);
        log.info("queryDsl select 쿼리 수행 후입니다.");
    }

    @Test
    @Transactional
    public void repositoryEvent() {
        log.info("repository 쿼리 수행 전입니다.");
        UserEntity member1 = userRepository.findById(savedMember.getId()).get();
        log.info("repository 쿼리 수행 후입니다.");

        log.info("repository update 쿼리 수행 전입니다.");
        member1.setAge(20);
        UserEntity updatedMember = userRepository.save(member1);
        log.info("repository update 쿼리 수행 후입니다.");
        log.info("repository select 쿼리 수행 전입니다.");
        UserEntity savedMember = userRepository.findById(updatedMember.getId()).get();
        log.info("repository select 쿼리 수행 후입니다.");
    }
}
