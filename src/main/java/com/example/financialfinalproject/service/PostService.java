package com.example.financialfinalproject.service;
import com.example.financialfinalproject.domain.dto.PostDetailDto;
import com.example.financialfinalproject.domain.dto.PostDto;
import com.example.financialfinalproject.domain.entity.Post;
import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.request.UserPostEditRequest;
import com.example.financialfinalproject.domain.request.UserPostRequest;
import com.example.financialfinalproject.domain.response.UserPostDetailResponse;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.exception.ErrorCode;
import com.example.financialfinalproject.repository.PostRepository;
import com.example.financialfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    public PostDto write(UserPostRequest userPostRequest, String name) {

        User user = userRepository.findByUserName(name).orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN, "잘못된 token 입니다."));

        Post post = Post.builder()
                .user(user)
                .title(userPostRequest.getTitle())
                .body(userPostRequest.getBody())
                .build();

        Post saved = postRepository.save(post);

        return new PostDto(saved.getId(), saved.getTitle(), saved.getBody());

    }

    public PostDetailDto detail(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, "해당 포스트가 없습니다."));

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

    public PostDto delete(Long id, String name) {

        Optional<Post> post = postRepository.findById(id);

        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, "Not founded"));


        if (post.isEmpty()) throw new AppException(ErrorCode.POST_NOT_FOUND, "해당 포스트가 없습니다.");
        if (!(user.getUserName().equals(post.get().getUser().getUserName())))
            throw new AppException(ErrorCode.INVALID_PERMISSION, "사용자가 권한이 없습니다.");

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

    public PostDto edit(Long id, String name, UserPostEditRequest userPostEditRequest) {

        Optional<Post> post = postRepository.findById(id);

        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, "Not founded"));

        if (post.isEmpty()) throw new AppException(ErrorCode.POST_NOT_FOUND, "해당 포스트가 없습니다.");
        if (!(user.getUserName().equals(post.get().getUser().getUserName())))
            throw new AppException(ErrorCode.INVALID_PERMISSION, "사용자가 권한이 없습니다.");


        Post postEdit = new Post();

//        postEdit.setId(id);
//        postEdit.setTitle(userPostEditRequest.getTitle());
//        postEdit.setBody(userPostEditRequest.getBody());
//        postEdit.setUser(user);
//        postEdit.getRegisteredAt(LocalDateTime.now());


        Post saved = postRepository.saveAndFlush(postEdit);

        return PostDto.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .body(saved.getBody())
                .build();
    }


}















