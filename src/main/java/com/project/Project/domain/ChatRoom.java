package com.project.Project.domain;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@SQLDelete(sql = "UPDATE chat_room SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class ChatRoom extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member host;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private Member guest;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessageList = new LinkedList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "latest_message_id")
    private ChatMessage latestMessage;

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
