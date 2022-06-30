package com.project.Project.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Review {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne(fetch = FetchType.LAZY)
    private ReviewForm reviewForm;

    @Embedded
    private AnonymousStatus anonymousStatus;
}
