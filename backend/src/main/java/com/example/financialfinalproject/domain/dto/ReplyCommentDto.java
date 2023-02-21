package com.example.financialfinalproject.domain.dto;

import com.example.financialfinalproject.domain.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReplyCommentDto {
    private Integer id;
    private Integer parent;
    private String comment;
    private String userName;
    private Long postId;
    private LocalDateTime createdAt;

    public ReplyCommentDto(Comment comment, Integer parent, String userName, Long postId) {
        this.id = comment.getId();
        this.parent = parent;
        this.comment = comment.getComment();
        this.userName = userName;
        this.postId = postId;
        this.createdAt = comment.getRegisteredAt();
    }
}