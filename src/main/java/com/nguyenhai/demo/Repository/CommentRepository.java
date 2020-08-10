package com.nguyenhai.demo.Repository;

import com.nguyenhai.demo.Entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    long countCommentByIdPost(String id);

    List<Comment> findAllByIdPostAndLevel(String idPos, Integer lever, Pageable pageable);

    List<Comment> findAllByIdPostAndIdAuthor(String idPos, String idAuthor);

    List<Comment> findAllByIdPostAndLevel(String idPos, Integer lever);

    List<Comment> findAllByIdIsInAndLevel(List<String> ids, Integer lever, Sort sort);

    List<Comment> findAllByIdIsIn(List<String> ids);

    void deleteByIdIsIn(List<String> ids);

    void deleteByIdPost(String id);
}
