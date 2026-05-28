package com.teutsch.ragassistant.controller;

import com.teutsch.ragassistant.dto.ChatRequest;
import com.teutsch.ragassistant.dto.ChatResponse;
import com.teutsch.ragassistant.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> ask(@RequestBody ChatRequest request) {
        ChatResponse response = chatService.ask(request.getQuestion());
        return ResponseEntity.ok(response);
    }
}
