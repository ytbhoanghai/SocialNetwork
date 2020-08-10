package com.nguyenhai.demo.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nguyenhai.demo.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.validation.constraints.NotEmpty;
import java.security.Principal;

@Controller
@RequestMapping(value = "chat")
public class ChatController {

    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public String requestPageChat() {
        return "chat";
    }

    @GetMapping(value = "chat-box/{idUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> openChatBox(@PathVariable String idUser, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(chatService.openChatBox(idUser, email)); // body of class
    }

    @GetMapping(value = "chat-box")
    public ResponseEntity<?> getListChatBox(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(chatService.getAllChatBox(email));
    }

    @DeleteMapping(value = "chat-box")
    public ResponseEntity<?> deleteChatBox(@RequestParam String idUser, Principal principal) {
        String email = principal.getName();
        chatService.deleteChatBox(idUser, email);
        return ResponseEntity.ok("chat box deleted successfully !!!");
    }

    @GetMapping(value = "chat-box/from")
    public ResponseEntity<?> getNextListChatBox(@RequestParam(required = false, defaultValue = "ytb") String idChatBox,
                                                @RequestParam Integer number,
                                                Principal principal) {

        String email = principal.getName();
        return ResponseEntity.ok(chatService.getNextListChatBox(idChatBox, number, email));
    }

    @GetMapping(value = "chat-box/un-seen", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> getNumberMessagesUnSeen(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(String.valueOf(chatService.getNumberMessagesUnSeen(email)));
    }

    @PutMapping(value = "chat-box/{idChatBox}/seen", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> seen(@PathVariable String idChatBox, @RequestParam Integer number, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(chatService.seen(idChatBox, number, email).toString());
    }

    @GetMapping(value = "chat-box/{idChatBox}/messages")
    public ResponseEntity<?> getMessages(@PathVariable String idChatBox,
                                         @RequestParam(required = false, defaultValue = "ytb") String fromMessage,
                                         @RequestParam Integer number,
                                         Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(chatService.getMessages(idChatBox, fromMessage, number, email));
    }

    @PostMapping(value = "chat-box/{idChatBox}/messages", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> createMessage(@PathVariable String idChatBox, @NotEmpty @RequestBody String content, Principal principal) throws JsonProcessingException {
        String email = principal.getName();
        return ResponseEntity.ok(chatService.createMessage(idChatBox, content, email));
    }
}
