package com.project.Project.domain.chat;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.Member;
import com.project.Project.domain.enums.MessageStatus;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@SQLDelete(sql = "UPDATE chat_message SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class ChatMessage extends BaseEntity {

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
    private MessageStatus readStatus;

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
