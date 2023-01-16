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
import java.util.Objects;

import static com.example.financialfinalproject.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
//    private final AlarmRepository alarmRepository;

    public CommentDto createComment(Long postId, String userName, CommentCreateRequest commentCreateRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(USERNAME_NOT_FOUND, USERNAME_NOT_FOUND.getMessage()));

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

    public Page<CommentDto> getAllItems(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));

        Page<Comment> comments = commentRepository.findAllByPost(post, pageable);
        Page<CommentDto> commentDtos = CommentDto.toDtoList(comments);
        return commentDtos;
    }

    public CommentUpdateResponse updateComment(Long postId, Integer commentId, CommentUpdateRequest commentUpdateRequest, String userName) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(COMMENT_NOT_FOUND, COMMENT_NOT_FOUND.getMessage()));

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(USERNAME_NOT_FOUND, USERNAME_NOT_FOUND.getMessage()));

        if (isMismatch(userName, comment)) {
            throw new AppException(INVALID_PERMISSION, INVALID_PERMISSION.getMessage());
        }
        comment.setComment(commentUpdateRequest.getComment());
        Comment updateComment = commentRepository.save(comment);
        return CommentUpdateResponse.toResponse(updateComment);
    }

    public boolean deleteComment(Long postId, Integer commentId, String userName) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(POST_NOT_FOUND, POST_NOT_FOUND.getMessage()));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(COMMENT_NOT_FOUND, COMMENT_NOT_FOUND.getMessage()));

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(USERNAME_NOT_FOUND, USERNAME_NOT_FOUND.getMessage()));

        if (isMismatch(userName, comment)) {
            throw new AppException(INVALID_PERMISSION, INVALID_PERMISSION.getMessage());
        }
        commentRepository.delete(comment);
        return true;


    }

    private static boolean isMismatch(String userName, Comment comment) {
        return !Objects.equals(comment.getUser().getUserName(), userName);
    }


}
