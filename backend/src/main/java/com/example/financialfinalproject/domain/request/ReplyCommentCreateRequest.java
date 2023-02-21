package com.example.financialfinalproject.domain.request;

import com.example.financialfinalproject.domain.entity.Comment;
import com.example.financialfinalproject.domain.entity.Post;
import com.example.financialfinalproject.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyCommentCreateRequest {
    private String replyComment;

    public Comment toEntity(User user, Post post, Comment parentId) {
        return Comment.builder()
                .comment(this.replyComment)
                .user(user)
                .post(post)
                .parent(parentId)
                .build();
    }
}