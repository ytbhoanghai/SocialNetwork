package com.nguyenhai.demo.Service.Implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nguyenhai.demo.Entity.*;
import com.nguyenhai.demo.Exception.CommentNotFoundException;
import com.nguyenhai.demo.Exception.InfoUserNotFoundException;
import com.nguyenhai.demo.Exception.PostCanNotSharedException;
import com.nguyenhai.demo.Exception.PostNotFoundException;
import com.nguyenhai.demo.Form.PostForm;
import com.nguyenhai.demo.Repository.*;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Response.CommentResponse;
import com.nguyenhai.demo.Response.PostResponse;
import com.nguyenhai.demo.Service.*;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.nguyenhai.demo.Entity.Notification.Type.*;
import static com.nguyenhai.demo.Entity.Post.Action.SHARE;
import static com.nguyenhai.demo.Entity.Post.Type.NEW_POST;
import static com.nguyenhai.demo.Entity.Post.Type.SHARED_POST;

@Service(value = "postService")
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private FeelingRepository feelingRepository;
    private PlaceLivedRepository placeLivedRepository;
    private PhotoGroupService photoGroupService;
    private InfoUserService infoUserService;
    private FileService fileService;
    private NotificationService notificationService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           CommentRepository commentRepository,
                           FeelingRepository feelingRepository,
                           PlaceLivedRepository placeLivedRepository,
                           PhotoGroupService photoGroupService,
                           InfoUserService infoUserService,
                           FileService fileService,
                           NotificationService notificationService) {

        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.feelingRepository = feelingRepository;
        this.placeLivedRepository = placeLivedRepository;
        this.photoGroupService = photoGroupService;
        this.infoUserService = infoUserService;
        this.fileService = fileService;
        this.notificationService = notificationService;
    }

    @Override
    public void createPost(PostForm postForm, String email) {
        InfoUser me = infoUserService.findByEmail(email);

        // save photo
        List<String> photos = postForm.getPhotos().stream()
                .map(s -> {
                    String id = UUID.randomUUID().toString();
                    try {
                        return saveAndConvertBase64ToImage(s, id);
                    } catch (IOException e) { return null; }
                })
                .collect(Collectors.toList());

        // tagFriends
        List<String> tagFriends = new ArrayList<>();
        if (!postForm.getTagFriends().isEmpty()) {
            tagFriends = infoUserService.findByIdIsIn(postForm.getTagFriends()).stream()
                    .map(InfoUser::getId)
                    .collect(Collectors.toList());
        }

        // feeling and checkin
        Feeling feeling = null;
        if (postForm.getIdFeeling() != null) {
            feeling = feelingRepository.findById(postForm.getIdFeeling())
                    .orElse(null);
        }
        PlaceLived checkin = null;
        if (postForm.getIdCheckin() != null) {
            checkin = placeLivedRepository.findById(postForm.getIdCheckin())
                    .orElse(null);

        }

        Post post = Post.builder()
                .id("P-" + UUID.randomUUID())
                .idAuthor(me.getId())
                .content(postForm.getContent())
                .photos(photos)
                .tagFriends(tagFriends)
                .feeling(feeling)
                .checkin(checkin)
                .interactive(Post.createDefaultInteractive())
                .dateCreated(new Date(System.currentTimeMillis()))
                .type(Post.Type.NEW_POST).build();

        postRepository.save(post);
        createAndSavePhotoGroup(post.getId(), me.getId(), photos);
        notifyNewPost(post);
    }

    @Override
    public void sharePost(String idPost, String email) {
        Post origin = postRepository.findById(idPost)
                .orElseThrow(() -> new PostNotFoundException(idPost));

        InfoUser user = infoUserService.findByEmail(email);
        InfoUser author = infoUserService.findById(origin.getIdAuthor());

        switch (origin.getType()) {
            case CHANGE_AVATAR:
                throw new PostCanNotSharedException();
            case SHARED_POST:
                sharePost(origin.getIdSharedPost(), email);
                return;
        }

        Post post = Post.builder()
                .id("P-" + UUID.randomUUID())
                .idAuthor(user.getId())
                .idSharedPost(origin.getId())
                .idUserOrigin(author.getId())
                .interactive(Post.createDefaultInteractive())
                .dateCreated(new Date())
                .type(SHARED_POST).build();

        postRepository.save(post);

        List<String> sharedList = origin.getInteractive().get(SHARE);
        if (!sharedList.contains(user.getId())) {
            sharedList.add(user.getId());
            postRepository.save(origin);
        }

        notifyNewPost(post);
        notifySharedPost(post, author, user);
    }

    @Override
    public void deletePost(String idPost, String email) {
        InfoUser user = infoUserService.findByEmail(email);
        Post post = postRepository.findById(idPost)
                .orElseThrow(() -> new PostNotFoundException(idPost));

        Assert.isTrue(user.getId().equals(post.getIdAuthor()), "Access Denied");
        if (post.getType() == NEW_POST) {
            postRepository.findAllByIdSharedPost(post.getId())
                    .forEach(post1 -> {
                        InfoUser u = infoUserService.findById(post1.getIdAuthor());
                        deletePost(post1.getId(), u.getEmail());
                    });
        }

        commentRepository.deleteByIdPost(post.getId());
        notificationService.deleteByPost(post);
        photoGroupService.deleteByPostId(post.getId());
        postRepository.delete(post);
    }

    @Override
    public void createPostForAvatar(String url, String email) {
        InfoUser me = infoUserService.findByEmail(email);

        Post post = Post.builder()
                .id("P-" + UUID.randomUUID())
                .idAuthor(me.getId())
                .photos(Collections.singletonList(url))
                .interactive(Post.createDefaultInteractive())
                .dateCreated(new Date(System.currentTimeMillis()))
                .type(Post.Type.CHANGE_AVATAR).build();

        postRepository.save(post);
        notifyNewPost(post);
    }

    @Override
    public PostResponse findPostById(String id, String email) {
        InfoUser user = infoUserService.findByEmail(email);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        // case block
        InfoUser user1 = infoUserService.findById(post.getIdAuthor());
        if (user1.getBlockedListFriendInfo().containsKey(user.getId())) {
            throw new InfoUserNotFoundException(user1.getId());
        }

        return convertPostToPostResponse(post, user.getId());
    }

    @Override
    public List<PostResponse> getPost(String id, Integer page, Integer number, String email) {
        InfoUser user = infoUserService.findById(id);
        InfoUser me = infoUserService.findByEmail(email);

        List<String> temp = new ArrayList<>(user.getListFollowing());
        temp.add(user.getId());

        PageRequest pageRequest = PageRequest.of(page, number, Sort.by(Sort.Direction.DESC, "dateCreated"));
        return postRepository.findAllByIdAuthorOrTagFriendsIsContaining(user.getId(), user.getId(), pageRequest).stream()
                .filter(post -> temp.contains(post.getIdAuthor()))
                .map(post -> convertPostToPostResponse(post, me.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getNextPost(String idUser, String idPost, Integer number, String email) {
        InfoUser user = infoUserService.findById(idUser);
        InfoUser me = infoUserService.findByEmail(email);

        List<String> temp = new ArrayList<>(user.getListFollowing());
        temp.add(user.getId());

        Post post = postRepository.findById(idPost)
                .orElseThrow(() -> new PostNotFoundException(idPost));

        List<PostResponse> result = new ArrayList<>();

        List<Post> posts = postRepository
                .findAllByIdAuthorOrTagFriendsIsContaining(
                        user.getId(),
                        user.getId(),
                        Sort.by(Sort.Direction.DESC, "dateCreated")).stream()
                .filter(p -> temp.contains(p.getIdAuthor()))
                .collect(Collectors.toList());

        int index = posts.indexOf(post);
        for (int i = index + 1; i < posts.size() && i < index + number + 1; i++) {
            result.add(convertPostToPostResponse(posts.get(i), me.getId()));
        }
        return result;
    }

    @Override
    public List<PostResponse> getTimeline(String idUser, String idPostStart, Integer number, String email) {
        InfoUser user = infoUserService.findById(idUser);
        InfoUser me = infoUserService.findByEmail(email);

        List<String> idFollowing = user.getListFollowing();
        idFollowing.add(user.getId());
        List<Post> posts = postRepository.findAllByIdAuthorIsIn(idFollowing, Sort.by(Sort.Direction.DESC, "dateCreated"));

        int start = 0;
        if (!idPostStart.equals("-1")) {
            Post post = postRepository.findById(idPostStart)
                    .orElseThrow(() -> new PostNotFoundException(idPostStart));
            start = posts.indexOf(post) + 1;
        }

        List<PostResponse> postResponses = new ArrayList<>();
        for (int i = start; i < posts.size() && i < start + number; i++) {
            postResponses.add(convertPostToPostResponse(posts.get(i), me.getId()));
        }

        return postResponses;
    }

    @Override
    public List<BasicUserInfoResponse> getInfoUserReactedOfPost(String id, Integer page, Integer number, Post.Action action) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        List<String> temp = new ArrayList<>();
        if (action == Post.Action.NONE) {
            for (Post.Action _a : post.getInteractive().keySet()) {
                if (_a != Post.Action.COMMENT && _a != SHARE) {
                    temp.addAll(post.getInteractive().get(_a));
                }
            }
        } else {
            temp.addAll(post.getInteractive().get(action));
        }

        return infoUserService.findByIdIsIn(temp).stream()
                .skip(page * number).limit(number)
                .map(BasicUserInfoResponse::build)
                .collect(Collectors.toList());
    }

    @Override
    public HashMap<String, String> createReactedPost(String id, Post.Action action, String email) throws JsonProcessingException {
        HashMap<String, String> result = new HashMap<>();

        if (action == SHARE || action == Post.Action.COMMENT) {
            result.put("error", "field not accepted");
            return result;
        }

        InfoUser m = infoUserService.findByEmail(email);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        if (action == Post.Action.NONE) {
            result.put("numberReacted", getOthersInfoFromPost(postRepository.save(post))
                    .get("numberFeelings"));
            result.put("result", String.valueOf(false));
            result.put("userReacted",
                    new ObjectMapper().writeValueAsString(getInfoUserReactedOfPost(post.getId(), 0, 15, Post.Action.NONE)));
            return result;
        }

        HashMap<Post.Action, List<String>> temp = post.getInteractive();
        for (Post.Action _a : temp.keySet()) {
            if (_a == SHARE
                    || _a == Post.Action.COMMENT
                    || _a == Post.Action.NONE
                    || _a == action) {
                continue;
            }

            temp.get(_a).remove(m.getId());
        }

        if (temp.get(action).contains(m.getId())) {
            temp.get(action).remove(m.getId());
            result.put("result", String.valueOf(false));

            notificationService
                    .deleteByTypeAndIdInteractiveAndPost(Notification.Type.REACTIVE_POST, m.getId(), post);
        } else {
            temp.get(action).add(m.getId());
            result.put("result", String.valueOf(true));

            notifyReactedPost(post, m);
        }

        result.put("numberReacted", getOthersInfoFromPost(postRepository.save(post))
                .get("numberFeelings"));
        result.put("userReacted",
                new ObjectMapper().writeValueAsString(getInfoUserReactedOfPost(post.getId(), 0, 15, Post.Action.NONE)));

        return result;
    }

    @Override
    public HashMap<String, String> createReactedComment(String idComment, String email) throws JsonProcessingException {
        // like comment
        HashMap<String, String> result = new HashMap<>();
        InfoUser user = infoUserService.findByEmail(email);
        Comment comment = commentRepository.findById(idComment)
                .orElseThrow(() -> new CommentNotFoundException(idComment));

        Post post = postRepository.findById(comment.getIdPost())
                .orElseThrow(() -> new PostNotFoundException(comment.getIdPost()));

        if (comment.getLikes().contains(user.getId())) {
            comment.getLikes()
                    .remove(user.getId());
            result.put("status", "false");
            notificationService.deleteByTypeAndIdInteractiveAndPostAndComment(REACTIVE_COMMENT, user.getId(), post, comment);
        } else {
            comment.getLikes()
                    .add(user.getId());
            result.put("status", "true");
            notifyReactedComment(post, comment, user);
        }

        Integer numberLiked = comment.getLikes().size();
        List<BasicUserInfoResponse> someUserLiked = infoUserService.findByIdIsIn(comment.getLikes()).stream()
                .limit(15)
                .map(BasicUserInfoResponse::build)
                .collect(Collectors.toList());

        result.put("numberLiked", String.valueOf(numberLiked));
        result.put("someUserLiked", new ObjectMapper().writeValueAsString(someUserLiked));

        commentRepository.save(comment);

        return result;
    }

    @Override
    public CommentResponse createComment(String id, String content, String email) {
        InfoUser user = infoUserService.findByEmail(email);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        List<String> temp = post.getInteractive().get(Post.Action.COMMENT);
        if (!temp.contains(user.getId())) {
            temp.add(user.getId());
        }

        Comment comment = Comment.builder()
                .id("C-" + UUID.randomUUID())
                .idAuthor(user.getId())
                .idPost(post.getId())
                .content(content)
                .likes(new ArrayList<>())
                .replies(new ArrayList<>())
                .level(0)
                .dateCreated(new Date(System.currentTimeMillis())).build();

        commentRepository.save(comment);
        postRepository.save(post);

        CommentResponse response = convertCommentToCommentResponse(comment, user.getId());
        notifyCommentPost(post, comment, user);

        return response;
    }

    @Override
    public List<CommentResponse> getCommentOfPost(String id, Integer page, Integer number, String email) {
        InfoUser user = infoUserService.findByEmail(email);

        Pageable pageable = PageRequest.of(page, number, Sort.by("dateCreated").descending());
        List<Comment> comments = commentRepository.findAllByIdPostAndLevel(id, 0, pageable);

        return comments.stream()
                .map(comment -> convertCommentToCommentResponse(comment, user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getCommentOfPost(String id, String fromComment, Integer number, String email) {
        InfoUser user = infoUserService.findByEmail(email);

        List<Comment> comments = commentRepository.findAllByIdPostAndLevel(id, 0);
        Collections.reverse(comments);
        Comment comment = commentRepository.findById(fromComment)
                .orElseThrow(() -> new CommentNotFoundException(fromComment));

        List<Comment> result = new ArrayList<>();
        int index = comments.indexOf(comment);

        for(int i = index + 1; i < index + number + 1; i++) {
            if (i >= comments.size()) break;
            result.add(comments.get(i));
        }

        return result.stream()
                .map(c -> convertCommentToCommentResponse(c, user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse createCommentInside(String id, String content, String email) {
        InfoUser user = infoUserService.findByEmail(email);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));

        Post post = postRepository.findById(comment.getIdPost())
                .orElseThrow(() -> new PostNotFoundException(comment.getIdPost()));

        Comment currentComment = Comment.builder()
                .id("CI-" + UUID.randomUUID())
                .idAuthor(user.getId())
                .idPost(comment.getIdPost())
                .idComment(comment.getIdComment() != null ? comment.getIdComment() : comment.getId())
                .idCommentReply(comment.getId())
                .content(content)
                .likes(new ArrayList<>())
                .replies(new ArrayList<>())
                .level(1)
                .dateCreated(new Date(System.currentTimeMillis())).build();

        List<String> temp = post.getInteractive().get(Post.Action.COMMENT);
        if (!temp.contains(user.getId())) {
            temp.add(user.getId());
        }

        currentComment = commentRepository.save(currentComment);
        comment.getReplies().add(currentComment.getId());

        if (comment.getIdComment() != null) {
            Comment commentOrigin = commentRepository.findById(comment.getIdComment())
                    .orElseThrow(() -> new CommentNotFoundException(comment.getIdComment()));
            commentOrigin.getReplies().add(currentComment.getId());
            commentRepository.save(commentOrigin);
        }

        commentRepository.save(comment);
        postRepository.save(post);


        notifyCommentInside(post, comment, currentComment, user);

        return convertCommentToCommentResponse(currentComment, user.getId());
    }

    @Override
    public List<CommentResponse> getCommentInside(String id, Integer page, Integer number, String email) {
        InfoUser user = infoUserService.findByEmail(email);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));

        Collections.reverse(comment.getReplies());
        List<String> idsComment = comment.getReplies().stream()
                .skip(page * number)
                .limit(number)
                .collect(Collectors.toList());

        return commentRepository.findAllByIdIsInAndLevel(idsComment, 1, Sort.by("dateCreated")).stream()
                .map(e -> convertCommentToCommentResponse(e, user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getCommentInside(String id, String fromComment, Integer number, String email) {
        InfoUser user = infoUserService.findByEmail(email);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        Comment comment1 = commentRepository.findById(fromComment)
                .orElseThrow(() -> new CommentNotFoundException(fromComment));

        Collections.reverse(comment.getReplies());
        List<String> result = new ArrayList<>();
        int index = comment.getReplies().indexOf(comment1.getId());
        for (int i = index + 1; i < comment.getReplies().size() && number > 0; i++) {
            if (i >= comment.getReplies().size()) break;
            result.add(comment.getReplies().get(i));
            number -= 1;
        }

        return commentRepository.findAllByIdIsIn(result).stream()
                .sorted(Comparator.comparing(Comment::getDateCreated))
                .map(c -> convertCommentToCommentResponse(c, user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(String id, String email) {
        InfoUser user = infoUserService.findByEmail(email);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        Post post = postRepository.findById(comment.getIdPost())
                .orElseThrow(() -> new PostNotFoundException(comment.getIdPost()));
        Assert.isTrue(
                comment.getIdAuthor().equals(user.getId()) ||
                post.getIdAuthor().equals(user.getId()), "Access Denied !!!");

        if (comment.getLevel().equals(0)) {
            comment.getReplies().forEach(s -> {
                commentRepository.findById(s).ifPresent(comment1 -> {
                    Post post1 = postRepository.findById(comment1.getIdPost())
                            .orElseThrow(() -> new PostNotFoundException(comment1.getIdPost()));
                    notificationService.deleteByIdAuthorAndPostAndComment(user.getId(), post1, comment1);
                    notificationService.deleteByIdInteractiveAndPostAndComment(user.getId(), post1, comment1);
                });
            });
            commentRepository.deleteByIdIsIn(comment.getReplies());
        } else {
            Comment parent = commentRepository.findById(comment.getIdComment())
                    .orElseThrow(() -> new CommentNotFoundException(comment.getIdComment()));
            parent.getReplies().removeIf(comment.getId()::equals);
            commentRepository.save(parent);
        }

        if (commentRepository.findAllByIdPostAndIdAuthor(comment.getIdPost(), user.getId())
                .size() == 1) {
            post.getInteractive().get(Post.Action.COMMENT).remove(user.getId());
            postRepository.save(post);
        }

        notificationService.deleteByIdAuthorAndPostAndComment(user.getId(), post, comment);
        notificationService.deleteByIdInteractiveAndPostAndComment(user.getId(), post, comment);
        commentRepository.deleteById(comment.getId());
    }

    @Override
    public long countByIdAuthor(String idUser) {
        return postRepository.countByIdAuthor(idUser);
    }

    @Override
    public HashMap<String, String> getCommentById(String id, String email) throws JsonProcessingException {
        InfoUser user = infoUserService.findByEmail(email);

        HashMap<String, String> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));

        if (comment.getLevel() == 0) {
            result.put("beforeLevel0", objectMapper.writeValueAsString(getListCommentBefore(comment, user.getId())));
            result.put("level0",
                    objectMapper.writeValueAsString(convertCommentToCommentResponse(comment, user.getId())));
        } else {
            Comment parent = commentRepository.findById(comment.getIdComment())
                    .orElseThrow(() -> new CommentNotFoundException(comment.getIdComment()));
            result.put("level0",
                    objectMapper.writeValueAsString(convertCommentToCommentResponse(parent, user.getId())));

            List<String> idsCommentLever1 = new ArrayList<>();
            Collections.reverse(parent.getReplies());
            int index = parent.getReplies().indexOf(comment.getId());

            for (int i = index - 20; i < index + 4; i++) {
                if (i < 0 || i >= parent.getReplies().size()) continue;
                idsCommentLever1.add(parent.getReplies().get(i));
            }

            List<CommentResponse> commentResponses = commentRepository.findAllByIdIsIn(idsCommentLever1).stream()
                    .sorted(Comparator.comparing(Comment::getDateCreated))
                    .map(c -> convertCommentToCommentResponse(c, user.getId()))
                    .collect(Collectors.toList());

            result.put("level1",
                    objectMapper.writeValueAsString(commentResponses));
            result.put("beforeLevel0", objectMapper.writeValueAsString(getListCommentBefore(parent, user.getId())));
            result.put("numberLeft", String.valueOf(parent.getReplies().size() - (index + 4)));
        }

        return result;
    }

    private void notifyNewPost(Post post) {
        Notification notification1 = Notification.build(post, null, post.getIdAuthor(), post.getIdAuthor(), Notification.Type.POST_READY);
        notificationService.save(notification1);
        InfoUser user = infoUserService.findById(notification1.getIdAuthor());
        notificationService.notificationAboutPost(notification1.getId(), user.getEmail());

        infoUserService.findByIdIsIn(post.getTagFriends()).forEach(infoUser -> {
            Notification notification2 = Notification.build(post, null, infoUser.getId(), post.getIdAuthor(), Notification.Type.TAG);
            notificationService.save(notification2);
            notificationService.notificationAboutPost(notification2.getId(), infoUser.getEmail());
        });
    }

    private void notifyReactedPost(Post post, InfoUser user) {
        if (post.getIdAuthor().equals(user.getId())) return;

        Notification notification = Notification.build(post, null, post.getIdAuthor(), user.getId(), REACTIVE_POST);
        notificationService.save(notification);
        InfoUser author = infoUserService.findById(post.getIdAuthor());
        notificationService.notificationAboutPost(notification.getId(), author.getEmail());
    }

    private void notifyCommentPost(Post post, Comment comment, InfoUser user) {
        if (!post.getIdAuthor().equals(user.getId())) {
            InfoUser infoAuthor = infoUserService.findById(post.getIdAuthor());
            Notification notification = Notification.build(post, comment, post.getIdAuthor(), user.getId(), COMMENT_POST);

            notificationService.save(notification);
            notificationService.notificationAboutPost(notification.getId(), infoAuthor.getEmail());
        }
        infoUserService.findByIdIsIn(post.getTagFriends()).forEach(u -> {
            if (!u.getId().equals(user.getId())) {
                Notification notification1 = Notification.build(post, comment, u.getId(), user.getId(), COMMENT_TAG);

                notificationService.save(notification1);
                notificationService.notificationAboutPost(notification1.getId(), u.getEmail());
            }
        });
    }

    private void notifyReactedComment(Post post, Comment comment, InfoUser user) {
        if (comment.getIdAuthor().equals(user.getId())) return;

        Notification notification = Notification.build(post, comment, comment.getIdAuthor(), user.getId(), REACTIVE_COMMENT);
        notificationService.save(notification);
        InfoUser author = infoUserService.findById(comment.getIdAuthor());
        notificationService.notificationAboutPost(notification.getId(), author.getEmail());
    }

    private void notifyCommentInside(Post post, Comment comment, Comment currentComment, InfoUser user) {
        if (comment.getIdAuthor().equals(currentComment.getIdAuthor())) return;

        Notification notification = Notification.build(post, currentComment, comment.getIdAuthor(), user.getId(), COMMENT_COMMENT);
        notificationService.save(notification);
        InfoUser author = infoUserService.findById(comment.getIdAuthor());
        notificationService.notificationAboutPost(notification.getId(), author.getEmail());
    }

    private void notifySharedPost(Post post, InfoUser user1, InfoUser user2) {
        if (user1.getId().equals(user2.getId())) return;
        Notification notification = Notification.build(post, null, user1.getId(), user2.getId(), SHARE_POST);
        notificationService.save(notification);
        notificationService.notificationAboutPost(notification.getId(), user1.getEmail());
    }

    private void createAndSavePhotoGroup(String postId, String idAuthor, List<String> photos) {
        if (!photos.isEmpty()) {
            String id = UUID.randomUUID().toString();
            PhotoGroup photoGroup = new PhotoGroup(id, postId, idAuthor, photos, new Date(System.currentTimeMillis()));
            photoGroupService.save(photoGroup);
        }
    }

    private PostResponse convertPostToPostResponse(Post post, String myId) {
        String id                               = post.getId();
        String content                          = post.getContent();
        Post.Type type                          = post.getType();
        Date dateCreated                        = post.getDateCreated();
        List<String> photos                     = post.getPhotos();
        Feeling feeling                         = post.getFeeling();
        PlaceLived checkin                      = post.getCheckin();
        Post.Action myAction                    = post.getAction(myId);
        Boolean ofMe                            = post.getIdAuthor().equals(myId);
        BasicUserInfoResponse infoAuthor        = BasicUserInfoResponse.build(infoUserService.findById(post.getIdAuthor()));
        List<BasicUserInfoResponse> tagFriends  = getListBasicUserInfoResponsesByIds(post.getTagFriends());
        HashMap<String, String> othersInfo      = getOthersInfoFromPost(post);

        if (post.getType() == SHARED_POST) {
            Post post1 = postRepository.findById(post.getIdSharedPost())
                    .orElseThrow(() -> new PostNotFoundException(post.getIdSharedPost()));
            content     = post1.getContent();
            photos      = post1.getPhotos();
        }

        return new PostResponse(id, infoAuthor, content, type, dateCreated, photos, tagFriends, feeling, checkin, myAction, ofMe, othersInfo);
    }

    private CommentResponse convertCommentToCommentResponse(Comment comment, String myId) {
        Post post = postRepository.findById(comment.getIdPost())
                .orElseThrow(() -> new PostNotFoundException(comment.getIdPost()));

        String id                                   = comment.getId();
        String content                              = comment.getContent();
        Boolean iLiked                              = comment.getLikes().contains(myId);
        Boolean ofMe                                = comment.getIdAuthor().equals(myId) | post.getIdAuthor().equals(myId);
        Integer numberCommentsInside                = comment.getReplies() != null ? comment.getReplies().size() : null;
        Integer numberLiked                         = comment.getLikes().size();
        Date dateCreated                            = comment.getDateCreated();
        BasicUserInfoResponse infoAuthor            = BasicUserInfoResponse.build(infoUserService.findById(comment.getIdAuthor()));
        BasicUserInfoResponse infoUserOrigin        = null;
        if (comment.getIdCommentReply() != null && !comment.getIdComment().equals(comment.getIdCommentReply())) {
            Optional<Comment> optionalCommentParent = commentRepository.findById(comment.getIdCommentReply());
            if (optionalCommentParent.isPresent()) {
                infoUserOrigin = BasicUserInfoResponse.build(
                        infoUserService.findById(optionalCommentParent.get().getIdAuthor()));
            }
        }

        List<BasicUserInfoResponse> someUserLiked   = infoUserService.findByIdIsIn(comment.getLikes()).stream()
                .limit(15)
                .map(BasicUserInfoResponse::build)
                .collect(Collectors.toList());

        return new CommentResponse(id, infoAuthor, infoUserOrigin, content, dateCreated, iLiked, ofMe, numberCommentsInside, numberLiked, someUserLiked);
    }

    private List<BasicUserInfoResponse> getListBasicUserInfoResponsesByIds(List<String> ids) {
        return infoUserService.findByIdIsIn(ids).stream()
                .map(BasicUserInfoResponse::build)
                .collect(Collectors.toList());
    }

    private HashMap<String, String> getOthersInfoFromPost(Post post) {
        HashMap<String, String> result = new HashMap<>();
        HashMap<Post.Action, List<String>> interactive = post.getInteractive();

        int numberFeelings = 0;
        for (Post.Action action : interactive.keySet()) {
            int size = interactive.get(action).size();
            switch (action) {
                case COMMENT:
                    result.put("numberComments", String.valueOf(commentRepository.countCommentByIdPost(post.getId())));
                    break;
                case SHARE:
                    result.put("numberShares", String.valueOf(size));
                    break;
                default:
                    numberFeelings += size;
            }
        }
        result.put("numberFeelings", String.valueOf(numberFeelings));

        if (post.getType() == SHARED_POST) {
            Post post1 = postRepository.findById(post.getIdSharedPost())
                    .orElseThrow(() -> new PostNotFoundException(post.getIdSharedPost()));
            String idUser = post1.getIdAuthor();
            InfoUser infoUser = infoUserService.findById(idUser);

            result.put("arg1", infoUser.getFullName());
            result.put("arg2", post1.getLink());
        }

        return result;
    }

    private String saveAndConvertBase64ToImage(String base64, String id) throws IOException {
        InputStream origin = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
        InputStream origin1 = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
        InputStream origin2 = new ByteArrayInputStream(Base64.getDecoder().decode(base64));

        Path path = Paths.get(FileService.PATH_SAVE_PHOTO.replace("{fileName}",  fileService.getFileNamePhoto(id)));
        Thumbnails.of(origin).width(1200).outputQuality(0.8F).outputFormat("jpg").toFile(path.toString());

        Path path1 = Paths.get(FileService.PATH_SAVE_PHOTO.replace("{fileName}",  fileService.getFileNamePhoto(id + "+350")));
        Thumbnails.of(origin1).size(350, 350).keepAspectRatio(false).outputFormat("jpg").toFile(path1.toString());

        Path path2 = Paths.get(FileService.PATH_SAVE_PHOTO.replace("{fileName}",  fileService.getFileNamePhoto(id + "+H350")));
        Thumbnails.of(origin2).height(350).outputQuality(1f).antialiasing(Antialiasing.ON).outputFormat("jpg").toFile(path2.toString());

        return "/file/photo/" + id;
    }

    private List<CommentResponse> getListCommentBefore(Comment comment, String userId) {
        return commentRepository.findAllByIdPostAndLevel(comment.getIdPost(), 0).stream()
                .filter(c -> c.getDateCreated().after(comment.getDateCreated()))
                .sorted(Comparator.comparing(Comment::getDateCreated))
                .map(c -> convertCommentToCommentResponse(c, userId))
                .collect(Collectors.toList());
    }
}
