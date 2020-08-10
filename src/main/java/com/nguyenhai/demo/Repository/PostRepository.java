package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findAllByIdSharedPost(String id);

    List<Post> findAllByIdAuthorOrTagFriendsIsContaining(String idAuthor, String idUserOrigin, Pageable pageable);

    List<Post> findAllByIdAuthorOrTagFriendsIsContaining(String idAuthor, String idUserOrigin, Sort sort);

    List<Post> findAllByIdAuthorIsIn(List<String> ids, Sort sort);

    long countByIdAuthor(String id);
}
