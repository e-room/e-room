package com.project.Project.db;

import com.project.Project.user.entity.UserEntity;
import com.project.Project.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
public class DBConnectionTest {

    UserRepository userRepository;

    @Autowired
    public DBConnectionTest(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    @Test()
    void setup(){
        UserEntity user1 = new UserEntity();
        user1.setAge(20);
        user1.setName("Test1");

        userRepository.save(user1);
        Optional<UserEntity> selectedUser = userRepository.findById(1L);
        UserEntity user = selectedUser.orElseThrow(()-> new RuntimeException("no user1"));
        assertThat(user.getName()).isEqualTo(user1.getName());

    }



}
