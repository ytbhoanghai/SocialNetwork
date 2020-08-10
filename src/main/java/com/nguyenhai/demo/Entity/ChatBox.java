package com.nguyenhai.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document
@TypeAlias("chatBox")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatBox {

    @Id
    private String id;
    private Date dateCreated;
    private Date lastAccess;
    private String idAuthor;
    private String idUser;
    private String latestContent;
    private Integer unSeen;

    public void addUnSeen(Integer number) {
        this.unSeen += number;
    }

    public static ChatBox createNewChatBox(String idAuthor, String idUser) {
        Date now = new Date();
        return ChatBox.builder()
                .id(UUID.randomUUID().toString())
                .dateCreated(now)
                .lastAccess(now)
                .idAuthor(idAuthor)
                .idUser(idUser)
                .latestContent("")
                .unSeen(0).build();
    }

}
