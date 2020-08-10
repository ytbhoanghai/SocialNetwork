package com.nguyenhai.demo.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nguyenhai.demo.Entity.Post;
import com.nguyenhai.demo.Form.PostForm;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Response.CommentResponse;
import com.nguyenhai.demo.Response.PostResponse;
import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;
import java.util.List;

public interface PostService {

    @Async("createPostExecutor")
    void createPost(PostForm postForm, String email);

    void sharePost(String idPost, String email);

    void deletePost(String idPost, String email);

    void createPostForAvatar(String url, String email);

    void deleteComment(String id, String email);

    long countByIdAuthor(String idUser);

    HashMap<String, String> createReactedPost(String id, Post.Action action, String email) throws JsonProcessingException;

    HashMap<String, String> createReactedComment(String idComment, String email) throws JsonProcessingException;

    HashMap<String, String> getCommentById(String id, String email) throws JsonProcessingException;

    CommentResponse createComment(String id, String content, String email);

    CommentResponse createCommentInside(String id, String content, String email);

    List<CommentResponse> getCommentOfPost(String id, Integer page, Integer number, String email);

    List<CommentResponse> getCommentOfPost(String id, String fromComment, Integer number, String email);

    List<CommentResponse> getCommentInside(String id, Integer page, Integer number, String email);

    List<CommentResponse> getCommentInside(String id, String fromComment, Integer number, String email);

    List<BasicUserInfoResponse> getInfoUserReactedOfPost(String id, Integer page, Integer number, Post.Action action);

    PostResponse findPostById(String id, String email);

    List<PostResponse> getPost(String id, Integer page, Integer number, String email);

    List<PostResponse> getNextPost(String idUser, String idPost, Integer number, String email);

    List<PostResponse> getTimeline(String idUser, String idPostStart, Integer number, String email);

}
