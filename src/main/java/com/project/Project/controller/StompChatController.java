package com.project.Project.controller;


import com.project.Project.dto.chat.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompChatController {
    private final SimpMessagingTemplate template;

    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDto messageDto){
        messageDto.setMessage(messageDto.getWriter() + " 님이 채팅방에 입장하셨습니다.");
        template.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), messageDto);
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto messageDto){
        template.convertAndSend("/sub/chat/room/"+messageDto.getRoomId(), messageDto);
    }

}
