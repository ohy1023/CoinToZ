package com.example.financialfinalproject.repository;

import com.example.financialfinalproject.domain.entity.Comment;
import com.example.financialfinalproject.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Transactional
    void deleteAllByPost(Post post);

    Page<Comment> findAllByPost(Post post, Pageable pageable);
}