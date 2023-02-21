package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.entity.DisLike;
import com.example.financialfinalproject.domain.entity.Like;
import com.example.financialfinalproject.domain.entity.Post;
import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.repository.DisLikeRepository;
import com.example.financialfinalproject.repository.LikeRepository;
import com.example.financialfinalproject.repository.PostRepository;
import com.example.financialfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.financialfinalproject.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final DisLikeRepository DisLikeRepository;


    public boolean addLikeCount(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage()));

        likeRepository.findByPostAndUser(post, user)
                .ifPresent((like) -> {
                    throw new AppException(DUPLICATED_LIKE_COUNT, DUPLICATED_LIKE_COUNT.getMessage());
                });

//        DisLikeRepository.findByPostAndUser(post, user)
//                .ifPresent((Dislike) -> {
//                    throw new AppException(DUPLICATED_DISLIKE_COUNT, DUPLICATED_DISLIKE_COUNT.getMessage());
//                });

       Like like =  likeRepository.save(Like.builder()
                .post(post)
                .user(user)
                .build());

       Long likeCount = likeRepository.countByPost(post);
       Long disCount= DisLikeRepository.countByPost(post);

       post.setLikeCount(likeCount);
       post.setDisLikeCount(disCount);

       postRepository.save(post);

        return true;
    }

    public boolean deleteLikeCount(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage()));
        Like like = likeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new AppException(LIKE_NOT_FOUND, LIKE_NOT_FOUND.getMessage()));
        likeRepository.delete(like);
        return true;
    }

    public Long viewLikeCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));

        return likeRepository.countByPost(post);
    }

    public boolean addDisLikeCount(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage()));

        DisLikeRepository.findByPostAndUser(post, user)
                .ifPresent((Dislike) -> {
                    throw new AppException(DUPLICATED_DISLIKE_COUNT, DUPLICATED_DISLIKE_COUNT.getMessage());
                });

//        likeRepository.findByPostAndUser(post, user)
//                .ifPresent((like) -> {
//                    throw new AppException(DUPLICATED_LIKE_COUNT, DUPLICATED_LIKE_COUNT.getMessage());
//                });

        DisLikeRepository.save(DisLike.builder()
                .post(post)
                .user(user)
                .build());

        Long likeCount = likeRepository.countByPost(post);
        Long disCount= DisLikeRepository.countByPost(post);

        post.setLikeCount(likeCount);
        post.setDisLikeCount(disCount);

        postRepository.save(post);

        return true;
    }

    public boolean deleteDisLikeCount(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage()));
        DisLike Dislike = DisLikeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new AppException(DISLIKE_NOT_FOUND, DISLIKE_NOT_FOUND.getMessage()));
        DisLikeRepository.delete(Dislike);
        return true;
    }

    public Long viewDisLikeCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));

        return DisLikeRepository.countByPost(post);
    }

    public List<Like> findAllPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));
        return likeRepository.findAllByPost(post);
    }
}
