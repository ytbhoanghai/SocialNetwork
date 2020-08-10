package com.nguyenhai.demo.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nguyenhai.demo.Entity.Post;
import com.nguyenhai.demo.Exception.InfoUserNotFoundException;
import com.nguyenhai.demo.Form.PostForm;
import com.nguyenhai.demo.Response.CommentResponse;
import com.nguyenhai.demo.Response.PostResponse;
import com.nguyenhai.demo.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/post")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String requestPostSinglePage(@RequestParam String id, Principal principal) {
        try {
            postService.findPostById(id, principal.getName());
        } catch (RuntimeException e) { return "redirect:/error"; }
        return "post";
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> countByIdAuthor(@RequestParam String idUser) {
        return ResponseEntity.ok(String.valueOf(postService.countByIdAuthor(idUser)));
    }

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPost(@Valid @RequestBody PostForm postForm, Principal principal) {
        String email = principal.getName();
        postService.createPost(postForm, email);
        return ResponseEntity.ok("the post is being processed, you will receive a notification when it is ready");
    }

    @PostMapping(value = "{id}/share", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> sharePost(@PathVariable String id, Principal principal) {
        String email = principal.getName();
        postService.sharePost(id, email);
        return ResponseEntity.ok("the post is being processed, you will receive a notification when it is ready");
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPostById(@PathVariable String id, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(postService.findPostById(id, email));
    }

    @DeleteMapping(value = "{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deletePostById(@PathVariable String id, Principal principal) {
        String email = principal.getName();
        postService.deletePost(id, email);
        return ResponseEntity.ok("post deleted successfully");
    }

    @GetMapping(value = "{id}/reacted", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getInfoUserReactedOfPost(@PathVariable String id,
                                              @RequestParam Integer page,
                                              @RequestParam Integer number,
                                              @RequestParam(required = false, defaultValue = "NONE") Post.Action action) {
        return ResponseEntity.ok(
                postService.getInfoUserReactedOfPost(id, page, number, action));
    }

    @PostMapping(value = "{id}/reacted", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReactedPost(@PathVariable String id, @RequestParam Post.Action action, Principal principal) throws JsonProcessingException {
        String email = principal.getName();
        HashMap<String, String> response = postService.createReactedPost(id, action, email);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "{id}/comment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> createComment(@PathVariable String id, @RequestBody String content, Principal principal) {
        content = content.trim();
        if (content.equals("")) {
            throw new ValidationException("content cannot be empty");
        }
        String email = principal.getName();
        return ResponseEntity.ok(postService.createComment(id, content, email));
    }

    @DeleteMapping(value = "comment/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteComment(@PathVariable String id, Principal principal) {
        String email = principal.getName();
        postService.deleteComment(id, email);
        return ResponseEntity.ok("deleted comment with id " + id);
    }

    @GetMapping(value = "{id}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCommentOfPost(@PathVariable String id, @RequestParam Integer page, @RequestParam Integer number, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(postService.getCommentOfPost(id, page, number, email));
    }

    @GetMapping(value = "{id}/comment/{from-comment}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNextCommentOfPost(@PathVariable String id, @PathVariable(value = "from-comment") String fromComment, @RequestParam Integer number, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(postService.getCommentOfPost(id, fromComment, number, email));
    }

    @PostMapping(value = "comment/{id}/reacted", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReactedComment(@PathVariable String id, Principal principal) throws JsonProcessingException {
        String email = principal.getName();
        HashMap<String, String> response = postService.createReactedComment(id, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "comment/{id}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCommentInside(@PathVariable String id, @RequestParam Integer page, @RequestParam Integer number, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(postService.getCommentInside(id, page, number, email));
    }

    @GetMapping(value = "comment/{id}/comment/{from-comment}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNextCommentInside(@PathVariable String id, @PathVariable(value = "from-comment") String fromComment, @RequestParam Integer number, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(postService.getCommentInside(id, fromComment, number, email));
    }

    @PostMapping(value = "comment/{id}/comment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> createCommentInside(@PathVariable String id, @RequestBody String content, Principal principal) {
        content = content.trim();
        if (content.equals("")) {
            throw new ValidationException("content cannot be empty");
        }
        String email = principal.getName();
        return ResponseEntity.ok(postService.createCommentInside(id, content, email));
    }

    @GetMapping(value = "comment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCommentById(@PathVariable String id, Principal principal) throws JsonProcessingException {
        String email = principal.getName();
        return ResponseEntity.ok(postService.getCommentById(id, email));
    }
}
