package com.project.Project.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
public class Room {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "room")
    private List<Review> reviewList = new LinkedList<>();

    @OneToMany(mappedBy = "room")
    private List<MemberRoom> memberList = new LinkedList<>();

    /**
     * 전용면적
     * 최대 유효 자릿수 : 10, 소수점 우측 자릿수 : 3
     */
    @Column(precision = 10, scale = 3)
    private BigDecimal netLeasableArea;

    @Embedded
    private Address address;

    /**
     * 빛 방향
     * 남향/동향 등
     */
    @Enumerated(EnumType.STRING)
    private LightDirection lightDirection;

    private Boolean hasElevator;

    private Integer managementFee;

    // TODO : 건물사진, 주변 상권(동영상) 관련 필드 추가 여부 질문
}
