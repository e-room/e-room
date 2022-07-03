package com.project.Project.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ChatMessage extends BaseEntity{

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member from;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member to;

    private String message;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;
}
