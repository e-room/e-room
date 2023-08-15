package com.project.Project.loader.checklist;

import com.project.Project.domain.checklist.CheckList;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChecklistLoader {

    private final JPAQueryFactory factory;


    public CheckList loadAllRelations(CheckList checklist) {
        checklist.getCheckListResponses().forEach(Hibernate::initialize);
        checklist.getCheckListImageList().forEach(Hibernate::initialize);
        checklist.getBuilding();
        return checklist;
    }

}
