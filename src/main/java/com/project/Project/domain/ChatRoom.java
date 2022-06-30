package com.project.Project.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ChatRoom {

    @Id @GeneratedValue
    private Long id;

    // 연관관계 설정하기

    @Enumerated(EnumType.STRING)
    private ReadStatus hostReadStatus;

    @Enumerated(EnumType.STRING)
    private ReadStatus guestReadStatus;

    private String latestMessage;
}
