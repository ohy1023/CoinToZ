package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.entity.DisLike;
import com.example.financialfinalproject.domain.entity.Like;
import com.example.financialfinalproject.domain.entity.Post;
import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.exception.ErrorCode;
import com.example.financialfinalproject.repository.DisLikeRepository;
import com.example.financialfinalproject.repository.LikeRepository;
import com.example.financialfinalproject.repository.PostRepository;
import com.example.financialfinalproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class LikeServiceTest {

    LikeService likeService;

    UserRepository userRepository = mock(UserRepository.class);
    PostRepository postRepository = mock(PostRepository.class);
    LikeRepository likeRepository = mock(LikeRepository.class);
    DisLikeRepository disLikeRepository = mock(DisLikeRepository.class);

    private final User user = User.builder()
            .id(1).userName("개발의민족").password("1234").email("test_Email")
            .build();
    private final Post post = Post.builder()
            .id(1L).title("testTitle").body("testBody").user(user)
            .build();
    private final Like like = Like.builder()
            .id(1)
            .post(post).user(user)
            .build();
    private final DisLike dislike = DisLike.builder()
            .post(post).user(user)
            .build();

    @BeforeEach
    void setUp() {
        likeService = new LikeService(userRepository, postRepository, likeRepository, disLikeRepository);
    }

    @Nested
    class test_Like {
        @Test
        @DisplayName("좋아요 입력 성공")
        void addLikeCount_sucess() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(likeRepository.findByPostAndUser(post, user)).thenReturn(Optional.empty());


            boolean likeResponse = likeService.addLikeCount(post.getId(), user.getEmail());

            assertTrue(likeResponse);
        }

        @Test
        @DisplayName("좋아요 입력 실패 : 좋아요 중복입력")
        void addLikeCount_fail() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(likeRepository.findByPostAndUser(post, user)).thenReturn(Optional.of(like));

            AppException appException = assertThrows(AppException.class, () -> {
                likeService.addLikeCount(post.getId(), user.getUserName());
            });
            assertEquals(ErrorCode.DUPLICATED_LIKE_COUNT, appException.getErrorCode());

        }

        @Test
        @DisplayName("좋아요 입력 실패 : 유저 아이디가 없음")
        void addLikeCount_fail2() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(likeRepository.findByPostAndUser(post, user)).thenReturn(Optional.empty());

            AppException appException = assertThrows(AppException.class, () -> {
                likeService.addLikeCount(post.getId(), user.getEmail());
            });
            assertEquals(ErrorCode.EMAIL_NOT_FOUND, appException.getErrorCode());
        }

        @Test
        @DisplayName("좋아요 입력 실패 : 포스트 아이디가 없음")
        void addLikeCount_fail3() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(postRepository.findById(any())).thenReturn(Optional.empty());
            when(likeRepository.findByPostAndUser(post, user)).thenReturn(Optional.empty());

            AppException appException = assertThrows(AppException.class, () -> {
                likeService.addLikeCount(post.getId(), user.getEmail());
            });
            assertEquals(ErrorCode.POST_NOT_FOUND, appException.getErrorCode());
        }

        @Test
        @DisplayName("좋아요 입력 실패 : 싫어요와 중복 입력")
        void addLikeCount_fail4() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(disLikeRepository.findByPostAndUser(post, user)).thenReturn(Optional.of(dislike));

            AppException appException = assertThrows(AppException.class, () -> {
                likeService.addLikeCount(post.getId(), user.getEmail());
            });
            assertEquals(ErrorCode.DUPLICATED_DISLIKE_COUNT, appException.getErrorCode());
        }

        @Test
        @DisplayName("좋아요 취소 성공")
        void deleteLikeCount() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(likeRepository.findByPostAndUser(post, user)).thenReturn(Optional.of(like));

            boolean likeResponse = likeService.deleteLikeCount(post.getId(), user.getEmail());
            assertTrue(likeResponse);
        }
    }

    @Nested
    class test_DisLike {
        @Test
        @DisplayName("싫어요 입력 성공")
        void addDisLikeCount_sucess() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(disLikeRepository.findByPostAndUser(post, user)).thenReturn(Optional.empty());


            boolean DislikeResponse = likeService.addDisLikeCount(post.getId(), user.getEmail());

            assertTrue(DislikeResponse);
        }

        @Test
        @DisplayName("싫어요 입력 실패 : 싫어요 중복입력")
        void addDisLikeCount_fail() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(disLikeRepository.findByPostAndUser(post, user)).thenReturn(Optional.of(dislike));

            AppException appException = assertThrows(AppException.class, () -> {
                likeService.addDisLikeCount(post.getId(), user.getEmail());
            });
            assertEquals(ErrorCode.DUPLICATED_DISLIKE_COUNT, appException.getErrorCode());

        }

        @Test
        @DisplayName("싫어요 입력 실패 : 유저 아이디가 없음")
        void addDisLikeCount_fail2() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(likeRepository.findByPostAndUser(post, user)).thenReturn(Optional.empty());

            AppException appException = assertThrows(AppException.class, () -> {
                likeService.addDisLikeCount(post.getId(), user.getEmail());
            });
            assertEquals(ErrorCode.EMAIL_NOT_FOUND, appException.getErrorCode());
        }

        @Test
        @DisplayName("싫어요 입력 실패 : 포스트 아이디가 없음")
        void addDisLikeCount_fail3() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(postRepository.findById(any())).thenReturn(Optional.empty());
            when(likeRepository.findByPostAndUser(post, user)).thenReturn(Optional.empty());

            AppException appException = assertThrows(AppException.class, () -> {
                likeService.addDisLikeCount(post.getId(), user.getEmail());
            });
            assertEquals(ErrorCode.POST_NOT_FOUND, appException.getErrorCode());
        }

        @Test
        @DisplayName("싫어요 입력 실패 : 좋아요와 중복 입력")
        void addDisLikeCount_fail4() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(likeRepository.findByPostAndUser(post, user)).thenReturn(Optional.of(like));

            AppException appException = assertThrows(AppException.class, () -> {
                likeService.addDisLikeCount(post.getId(), user.getEmail());
            });
            assertEquals(ErrorCode.DUPLICATED_LIKE_COUNT, appException.getErrorCode());
        }

        @Test
        @DisplayName("싫어요 취소 성공")
        void deleteDisLikeCount() {
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(postRepository.findById(any())).thenReturn(Optional.of(post));
            when(disLikeRepository.findByPostAndUser(post, user)).thenReturn(Optional.of(dislike));

            boolean DislikeResponse = likeService.deleteDisLikeCount(post.getId(), user.getEmail());
            assertTrue(DislikeResponse);

        }
    }
}