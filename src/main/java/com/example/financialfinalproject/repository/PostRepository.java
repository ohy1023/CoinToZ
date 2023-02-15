package com.example.financialfinalproject.repository;

import com.example.financialfinalproject.domain.entity.Post;
import com.example.financialfinalproject.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository <Post,Long> {
    void deleteById (Long id);
    Page<Post> findByUserId (Long id, Pageable pageable);

    List<Post> findAllByUser(User user);

}
