//package com.example.financialfinalproject.service;
//
//import com.example.financialfinalproject.domain.entity.Comment;
//import com.example.financialfinalproject.domain.entity.Post;
//import com.example.financialfinalproject.domain.entity.User;
//import com.example.financialfinalproject.domain.request.CommentCreateRequest;
//import com.example.financialfinalproject.domain.request.CommentUpdateRequest;
//import com.example.financialfinalproject.exception.AppException;
//import com.example.financialfinalproject.exception.ErrorCode;
//import com.example.financialfinalproject.repository.CommentRepository;
//import com.example.financialfinalproject.repository.PostRepository;
//import com.example.financialfinalproject.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class CommentServiceTest {
//
//    CommentService commentService;
//
//    PostRepository postRepository = mock(PostRepository.class);
//    UserRepository userRepository = mock(UserRepository.class);
//    CommentRepository commentRepository = mock(CommentRepository.class);
//
//    @BeforeEach
//    void setUp() {
//        commentService = new CommentService(userRepository, postRepository, commentRepository);
//    }
//
//    CommentCreateRequest commentCreateRequest = new CommentCreateRequest("testComment");
//    CommentUpdateRequest commentUpdateRequest = new CommentUpdateRequest("testComment");
//
//    User user = User.builder()
//            .id(1)
//            .email("test_email")
//            .userName("개발의민족")
//            .password("1234")
//            .build();
//    Post post = Post.builder()
//            .id(1L)
//            .title("test title")
//            .body("test body")
//            .user(user)
//            .build();
//    Comment comment = Comment.builder()
//            .id(1)
//            .comment("testComment")
//            .post(post)
//            .user(user)
//            .build();
//
//    @Test
//    @DisplayName("댓글 작성 실패 : 유저가 존재하지 않음")
//    void createComment_fail() {
//        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
//        when(postRepository.findById(any())).thenReturn(Optional.of(post));
//        when(commentRepository.save(any())).thenReturn(comment);
//
//        AppException exception = assertThrows(AppException.class, () ->
//                commentService.createComment(post.getId(),user.getUserName(),commentCreateRequest));
//        assertEquals(ErrorCode.EMAIL_NOT_FOUND, exception.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("댓글 작성 실패 : 포스트가 존재하지 않음")
//    void createComment_fail2() {
//        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
//        when(postRepository.findById(any())).thenReturn(Optional.empty());
//        when(commentRepository.save(any())).thenReturn(comment);
//
//        AppException exception = assertThrows(AppException.class, () ->
//                commentService.createComment(post.getId(),user.getEmail(),commentCreateRequest));
//        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("댓글 수정 실패 : 유저가 존재하지 않음")
//    void updateComment_fail() {
//        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
//        when(postRepository.findById(any())).thenReturn(Optional.of(post));
//        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
//
//        AppException exception = assertThrows(AppException.class, () ->
//                commentService.updateComment(post.getId(),comment.getId(),commentUpdateRequest, user.getUserName()));
//        assertEquals(ErrorCode.EMAIL_NOT_FOUND, exception.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("댓글 수정 실패 : 댓글이 존재하지 않음")
//    void updateComment_fail2() {
//        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
//        when(postRepository.findById(any())).thenReturn(Optional.of(post));
//        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());
//
//        AppException exception = assertThrows(AppException.class, () ->
//                commentService.updateComment(post.getId(),comment.getId(),commentUpdateRequest, user.getEmail()));
//        assertEquals(ErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("댓글 수정 실패 : 포스트가 존재하지 않음")
//    void updateComment_fail3() {
//        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
//        when(postRepository.findById(any())).thenReturn(Optional.empty());
//        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
//
//        AppException exception = assertThrows(AppException.class, () ->
//                commentService.updateComment(post.getId(),comment.getId(),commentUpdateRequest, user.getEmail()));
//        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 실패 : 유저가 존재하지 않음")
//    void deleteComment_fail() {
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
//        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
//        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
//
//        AppException exception = assertThrows(AppException.class, () ->
//                commentService.deleteComment(post.getId(),comment.getId(), user.getUserName()));
//        assertEquals(ErrorCode.EMAIL_NOT_FOUND, exception.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 실패 : 댓글이 존재하지 않음")
//    void deleteComment_fail2() {
//        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
//        when(postRepository.findById(any())).thenReturn(Optional.of(post));
//        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());
//
//
//        AppException exception = assertThrows(AppException.class, () ->
//                commentService.deleteComment(post.getId(),comment.getId(), user.getEmail()));
//        assertEquals(ErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 실패 : 포스트가 존재하지 않음")
//    void deleteComment_fail3() {
//        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
//        when(postRepository.findById(any())).thenReturn(Optional.empty());
//        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());
//
//        AppException exception = assertThrows(AppException.class, () ->
//                commentService.deleteComment(post.getId(),comment.getId(), user.getEmail()));
//        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
//    }
//
//}