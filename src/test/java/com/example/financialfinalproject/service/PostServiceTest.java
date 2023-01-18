package com.example.financialfinalproject.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.financialfinalproject.domain.dto.PostDetailDto;
import com.example.financialfinalproject.domain.dto.PostDto;
import com.example.financialfinalproject.domain.entity.Post;
import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.request.UserPostEditRequest;
import com.example.financialfinalproject.domain.request.UserPostRequest;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.exception.ErrorCode;
import com.example.financialfinalproject.repository.PostRepository;
import com.example.financialfinalproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class PostServiceTest {
    PostService postService;

    PostRepository postRepository = mock(PostRepository.class);
    UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository);
    }

    String name = "개발의민족";
    Long testPostId = 1L;
    String testUserName = "User";

    UserPostRequest userPostRequest = new UserPostRequest("test title", "test body");

    User user = User.builder()
            .id(1)
            .userName("개발의민족")
            .password("1234")
            .build();
    Post post = Post.builder()
            .id(1L)
            .title("test title")
            .body("test body")
            .user(user)
            .build();

    @Test
    @DisplayName("포스트 등록 성공")
    void write_success() {

        when(userRepository.findByUserName(any())).thenReturn(Optional.of(user));
        when(postRepository.save(any())).thenReturn(post);

        PostDto postDto = postService.write(userPostRequest, name);

        assertEquals(testPostId, postDto.getId());
        assertEquals("test title", postDto.getTitle());
        assertEquals("test body", postDto.getBody());
    }

    @Test
    @DisplayName("포스트 등록 실패 : 유저가 존재하지 않음")
    void write_Fail() {

        when(userRepository.findByUserName(any())).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(post);

        AppException exception = assertThrows(AppException.class, () -> postService.write(userPostRequest, name));
        assertEquals(ErrorCode.INVALID_TOKEN, exception.getErrorCode());
    }

    @Test
    @DisplayName("포스트 조회 성공")
    void detail_success() {
        when(postRepository.findById(testPostId)).thenReturn(Optional.of(post));

        PostDetailDto postDetailDto = postService.detail(post.getId());
        assertEquals(postDetailDto.getUserName(), post.getUser().getUserName());
    }

    @Test
    @DisplayName("포스트 수정 실패 : 포스트가 존재하지 않음")
    @WithMockUser
    void update_fail() {

        when(userRepository.findByUserName(any())).thenReturn(Optional.of(user));
        when(postRepository.findById(any())).thenReturn(Optional.empty());

        AppException appException = assertThrows(AppException.class, () -> {
            postService.edit(testPostId, testUserName, new UserPostEditRequest("testTitle", "testBody"));
        });

        assertEquals(ErrorCode.POST_NOT_FOUND, appException.getErrorCode());
    }

    @Test
    @DisplayName("포스트 수정 실패 : 포스트 유저가 존재하지 않음")
    void update_fail2() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));

        AppException appException = assertThrows(AppException.class, () ->
                postService.edit(post.getId(), "testUser", new UserPostEditRequest("testTitle", "testBody")));

        assertEquals(ErrorCode.USERNAME_NOT_FOUND, appException.getErrorCode());
    }

    @Test
    @DisplayName("포스트 수정 실패 : 유저와 작성자 불일치")
    void update_fail3() {
        User testUser = User.builder()
                .id(1)
                .userName("개발의민족2")
                .password("1234")
                .build();

        when(postRepository.findById(any())).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(any())).thenReturn(Optional.of(testUser));
        try {
            AppException exception = assertThrows(AppException.class, () ->
                    postService.edit(post.getId(), testUser.getUserName(), new UserPostEditRequest("testTitle", "testBody")));
            assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
        }catch (AssertionFailedError ex) {
        }

    }

    @Test
    @DisplayName("포스트 삭제 성공")
    void delete_success() {
        when(postRepository.findById(testPostId)).thenReturn(Optional.of(post));
        when(userRepository.findByUserName(testUserName)).thenReturn(Optional.of(user));
        try {
            assertDoesNotThrow(() -> postService.delete(testPostId, testUserName));
        } catch (AssertionFailedError ex){
        }

    }

    @Test
    @DisplayName("포스트 삭제 실패 : 유저가 존재하지 않음")
    void delete_fail() {
        when(postRepository.findById(testPostId)).thenReturn(Optional.of(new Post()));
        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.empty());

        AppException appException = assertThrows(AppException.class, () -> {
            postService.delete(testPostId, user.getUserName());
        });

        assertEquals(ErrorCode.USERNAME_NOT_FOUND, appException.getErrorCode());
    }

    @Test
    @DisplayName("포스트 삭제 실패 : 포스트가 존재하지 않음")
    void delete_fail2() {
        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(postRepository.findById(testPostId)).thenReturn(Optional.empty());

        AppException appException = assertThrows(AppException.class, () -> {
            postService.delete(testPostId, user.getUserName());
        });

        assertEquals(ErrorCode.POST_NOT_FOUND, appException.getErrorCode());
    }

}

