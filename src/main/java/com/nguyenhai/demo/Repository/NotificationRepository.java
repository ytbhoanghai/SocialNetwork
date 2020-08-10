package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.Comment;
import com.nguyenhai.demo.Entity.Notification;
import com.nguyenhai.demo.Entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByTypeAndIdAuthorAndPost(Notification.Type type, String idAuthor, Post post);

    List<Notification> findByIdAuthor(String s);

    List<Notification> findByIdInteractiveAndTypeIsNotIn(String idInteracted, List<Notification.Type> types, Pageable pageable);

    List<Notification> findByIdInteractiveAndTypeIsNotIn(String idInteracted, List<Notification.Type> types);

    void deleteByTypeAndIdInteractiveAndPost(Notification.Type type, String idInteractive, Post post);

    void deleteByTypeAndIdInteractiveAndPostAndComment(Notification.Type type, String idInteracted, Post post, Comment comment);

    void deleteByIdAuthorAndPostAndComment(String idAuthor, Post post, Comment comment);

    void deleteByIdInteractiveAndPostAndComment(String idInteractive, Post post, Comment comment);

    void deleteByPost(Post post);
}
