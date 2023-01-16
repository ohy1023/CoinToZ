package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.dto.PostDetailDto;
import com.example.financialfinalproject.domain.dto.PostDto;
import com.example.financialfinalproject.domain.entity.Post;
import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.request.UserPostEditRequest;
import com.example.financialfinalproject.domain.request.UserPostRequest;
import com.example.financialfinalproject.domain.response.UserPostDetailResponse;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.repository.PostRepository;
import com.example.financialfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.financialfinalproject.domain.enums.UserRole.*;
import static com.example.financialfinalproject.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    @Transactional
    public PostDto write(UserPostRequest userPostRequest, String name) {

        User user = userRepository.findByUserName(name).orElseThrow(() -> new AppException(INVALID_TOKEN, "잘못된 token 입니다."));

        Post post = Post.builder()
                .user(user)
                .title(userPostRequest.getTitle())
                .body(userPostRequest.getBody())
                .build();

        Post saved = postRepository.save(post);

        return new PostDto(saved.getId(), saved.getTitle(), saved.getBody());

    }

    public PostDetailDto detail(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(POST_NOT_FOUND, "해당 포스트가 없습니다."));

        PostDetailDto postDetailDto = PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUserName())
                .createdAt(post.getRegisteredAt())
                .lastModifiedAt(post.getUpdatedAt())
                .build();

        return postDetailDto;

    }

    @Transactional
    public PostDto delete(Long id, String name) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, "해당 포스트가 없습니다."));

        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(USERNAME_NOT_FOUND, "Not founded"));

        log.info("isNotAdmin:{}", !user.getUserRole().equals(ADMIN));
        log.info("isNotMatchName:{}", !name.equals(post.getUser().getUserName()));

        if (checkAuth(name, post, user))
            throw new AppException(INVALID_PERMISSION, "사용자가 권한이 없습니다.");

        postRepository.deleteById(id);

        return PostDto.builder()
                .id(id)
                .build();
    }

    public List<UserPostDetailResponse> list(Pageable pageable) {

        Page<Post> list = postRepository.findAll(pageable);

        List<UserPostDetailResponse> responseList = list.stream()
                .map(lists -> UserPostDetailResponse.builder()
                        .id(lists.getId())
                        .title(lists.getTitle())
                        .body(lists.getBody())
                        .userName(lists.getUser().getUserName())
                        .createdAt(lists.getRegisteredAt())
                        .lastModifiedAt(lists.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return responseList;
    }



    @Transactional
    public PostDto edit(Long id, String name, UserPostEditRequest userPostEditRequest) {
        log.info(userPostEditRequest.getTitle());
        log.info(userPostEditRequest.getBody());

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, "해당 포스트가 없습니다."));

        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(USERNAME_NOT_FOUND, "Not founded"));

        log.info("isNotAdmin:{}", !user.getUserRole().equals(ADMIN));
        log.info("isNotMatchName:{}", !name.equals(post.getUser().getUserName()));

        if (checkAuth(name, post, user))
            throw new AppException(INVALID_PERMISSION, "사용자가 권한이 없습니다.");

        post.updatePost(userPostEditRequest.getTitle(), userPostEditRequest.getBody());

        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .build();
    }

    private static boolean checkAuth(String userName, Post post, User user) {
        return !user.getUserRole().equals(ADMIN) && !userName.equals(post.getUser().getUserName());
    }


}















