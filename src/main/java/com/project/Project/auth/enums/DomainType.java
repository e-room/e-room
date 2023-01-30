package com.project.Project.auth.enums;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.room.Room;

public enum DomainType {

    ALL("All"),
    BUILDING(Building.class.getName()),
    REVIEW(Review.class.getName()),
    ROOM(Room.class.getName()),
    MEMBER(Member.class.getName()),
    NONE("None");

    private String name;


    DomainType(String name) {
        this.name = name;
    }

    /**
     * @todo: EntityType 보고 적당한 DomainType으로 바꿔주는 메서드
     */
    public DomainType from(Class<?> entityClass) {
        return null;
    }
}
