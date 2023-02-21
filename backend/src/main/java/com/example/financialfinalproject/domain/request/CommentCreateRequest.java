package com.example.financialfinalproject.domain.request;

import com.example.financialfinalproject.domain.entity.Post;
import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {
    private String comment;

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .comment(this.comment)
                .user(user)
                .post(post)
                .build();
    }
}