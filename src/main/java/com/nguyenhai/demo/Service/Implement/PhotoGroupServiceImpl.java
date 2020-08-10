package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Entity.PhotoGroup;
import com.nguyenhai.demo.Entity.Post;
import com.nguyenhai.demo.Exception.PostNotFoundException;
import com.nguyenhai.demo.Repository.CommentRepository;
import com.nguyenhai.demo.Repository.PhotoGroupRepository;
import com.nguyenhai.demo.Repository.PostRepository;
import com.nguyenhai.demo.Response.PhotoResponse;
import com.nguyenhai.demo.Service.InfoUserService;
import com.nguyenhai.demo.Service.PhotoGroupService;
import com.nguyenhai.demo.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.nguyenhai.demo.Entity.Post.Type.SHARED_POST;

@Service(value = "photoGroupService")
public class PhotoGroupServiceImpl implements PhotoGroupService {

    private PhotoGroupRepository photoGroupRepository;
    private PostRepository postRepository;
    private InfoUserService infoUserService;
    private CommentRepository commentRepository;

    @Autowired
    public PhotoGroupServiceImpl(
            PhotoGroupRepository photoGroupRepository,
            PostRepository postRepository,
            InfoUserService infoUserService,
            CommentRepository commentRepository) {

        this.photoGroupRepository = photoGroupRepository;
        this.postRepository = postRepository;
        this.infoUserService = infoUserService;
        this.commentRepository = commentRepository;
    }

    @Override
    public PhotoGroup save(PhotoGroup photoGroup) {
        return photoGroupRepository.save(photoGroup);
    }

    @Override
    public void deleteByPostId(String postId) {
        photoGroupRepository.deleteByPostId(postId);
    }

    @Override
    public List<PhotoResponse> findPhotos(String idUser, Integer page, Integer number) {
        PageRequest pageRequest = PageRequest.of(page, number, Sort.by(Sort.Direction.DESC, "dateCreated"));
        return photoGroupRepository.findAllByIdAuthor(idUser, pageRequest).stream()
                .map(photoGroup -> {
                    Post post = postRepository.findById(photoGroup.getPostId())
                            .orElseThrow(() -> new PostNotFoundException(photoGroup.getPostId()));
                    HashMap<String, String> othersInfo = getOthersInfoFromPost(post);
                    return PhotoResponse.build(photoGroup.getPhotos().get(0), post.getLink(), othersInfo);
                })
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
}
