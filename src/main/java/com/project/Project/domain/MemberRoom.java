package com.project.Project.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class MemberRoom {

    @Id @GeneratedValue
    private Long id;

    // 연관관계 설정하기
}
