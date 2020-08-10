package com.nguyenhai.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Document
@TypeAlias("post")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    private String id;
    private String idAuthor;
    private String content;
    private String idSharedPost;
    private String idUserOrigin;
    private List<String> photos;
    private List<String> tagFriends;
    @DBRef
    private Feeling feeling;
    @DBRef
    private PlaceLived checkin;
    private HashMap<Action, List<String>> interactive;
    private Date dateCreated;
    private Type type;

    public enum Action {
        COMMENT, SHARE, LIKE, LOVE, HAPPY, HAHA, THINK, SADE, LOVELY, NONE
    }

    public enum Type {
        SHARED_POST, CHANGE_AVATAR, NEW_POST
    }

    public static HashMap<Action, List<String>> createDefaultInteractive() {
        HashMap<Post.Action, List<String>> interactive = new HashMap<>();
        for(Action action : Action.values()) {
            interactive.put(action, new ArrayList<>());
        }

        return interactive;
    }

    public Action getAction(String id) {
        for (Action action : interactive.keySet()) {
            if (action == Action.COMMENT || action == Action.SHARE) continue;
            List<String> data = interactive.get(action);
            if (data.contains(id)) {
                return action;
            }
        }
        return Action.NONE;
    }

    public String getLink() {
        return "post?id=" + id;
    }
}
