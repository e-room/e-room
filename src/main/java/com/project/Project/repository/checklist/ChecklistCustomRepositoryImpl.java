package com.project.Project.repository.checklist;

import com.project.Project.domain.checklist.CheckListImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.List;

import static com.project.Project.domain.checklist.QCheckListImage.checkListImage;

@Repository
@RequiredArgsConstructor
public class ChecklistCustomRepositoryImpl implements ChecklistCustomRepository {
    private final EntityManager em;
    private final JPAQueryFactory factory;

    public Long countByChecklistId(Long checklistId) {
        return factory.select(checkListImage.count())
                .from(checkListImage)
                .where(checkListImage.checkList.id.eq(checklistId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }

    @Override
    public List<CheckListImage> getCheckListImagesWithLock(Long checklistId) {
        return factory.select(checkListImage)
                .from(checkListImage)
                .where(checkListImage.checkList.id.eq(checklistId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetch();
    }
}
