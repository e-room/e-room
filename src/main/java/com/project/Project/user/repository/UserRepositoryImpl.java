package com.project.Project.user.repository;

import com.project.Project.user.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.project.Project.user.entity.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {
    private final JPAQueryFactory factory;

    public void updateUser(UserEntity user, Integer age) {
        user.setAge(age);
        factory.update(userEntity)
                .where(userEntity.eq(user))
                .set(userEntity.age, age)
                .execute();
    }

    public UserEntity selectUser(Long id) {
        return factory.selectFrom(userEntity)
                .where(userEntity.id.eq(id))
                .fetch().get(0);
    }

}

