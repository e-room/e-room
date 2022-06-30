package com.project.Project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
public class ChatRoom {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member host;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private Member guest;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatContent> chatContentList = new LinkedList<>();

    @Enumerated(EnumType.STRING)
    private ReadStatus hostReadStatus;

    @Enumerated(EnumType.STRING)
    private ReadStatus guestReadStatus;

    private String latestMessage;
}
