package com.example.financialfinalproject.service;


import com.example.financialfinalproject.domain.dto.CommentDto;
import com.example.financialfinalproject.domain.entity.Comment;
import com.example.financialfinalproject.domain.entity.Post;
import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.request.CommentCreateRequest;
import com.example.financialfinalproject.domain.request.CommentUpdateRequest;
import com.example.financialfinalproject.domain.response.CommentUpdateResponse;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.repository.CommentRepository;
import com.example.financialfinalproject.repository.PostRepository;
import com.example.financialfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.example.financialfinalproject.domain.enums.UserRole.ADMIN;
import static com.example.financialfinalproject.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
//    private final AlarmRepository alarmRepository;

    @Transactional
    public CommentDto createComment(Long postId, String email, CommentCreateRequest commentCreateRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage()));

        Comment savedComment = commentRepository.save(commentCreateRequest.toEntity(user, post));

//        alarmRepository.save(Alarm.builder()
//                .user(post.getUser())
//                .alarmType(NEW_COMMENT_ON_POST)
//                .text(NEW_COMMENT_ON_POST.getAlarmText())
//                .fromUserId(user.getId())
//                .targetId(post.getId())
//                .build());

        return CommentDto.toCommentDto(savedComment);
    }

    @Transactional(readOnly = true)
    public Page<CommentDto> getAllItems(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));

        Page<Comment> comments = commentRepository.findAllByPost(post, pageable);
        Page<CommentDto> commentDtos = CommentDto.toDtoList(comments);
        return commentDtos;
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long postId, Integer commentId, CommentUpdateRequest commentUpdateRequest, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(COMMENT_NOT_FOUND, COMMENT_NOT_FOUND.getMessage()));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage()));

        if (checkAuth(email, comment, user)) {
            throw new AppException(INVALID_PERMISSION, INVALID_PERMISSION.getMessage());
        }

        comment.updateComment(commentUpdateRequest.getComment());
        return CommentUpdateResponse.toResponse(comment);
    }

    @Transactional
    public boolean deleteComment(Long postId, Integer commentId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(COMMENT_NOT_FOUND, COMMENT_NOT_FOUND.getMessage()));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage()));

        if (checkAuth(email, comment, user)) {
            throw new AppException(INVALID_PERMISSION, INVALID_PERMISSION.getMessage());
        }
        commentRepository.delete(comment);
        return true;


    }

    private static boolean checkAuth(String email, Comment comment, User user) {
        return !user.getUserRole().equals(ADMIN) && !email.equals(comment.getUser().getEmail());
    }


}
