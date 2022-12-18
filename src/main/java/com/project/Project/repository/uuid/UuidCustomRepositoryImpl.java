package com.project.Project.repository.uuid;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.project.Project.domain.QUuid.uuid1;

@Repository
@RequiredArgsConstructor
public class UuidCustomRepositoryImpl implements UuidCustomRepository {

    private final EntityManager em;
    private final JPAQueryFactory factory;


    @Override
    public Boolean exist(String uuid) {
        Integer fetchOne = factory.selectOne()
                .from(uuid1)
                .where(uuid1.uuid.eq(uuid))
                .fetchFirst();
        return fetchOne != null;
    }
}
