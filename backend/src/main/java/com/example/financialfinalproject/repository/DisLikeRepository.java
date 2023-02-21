package com.example.financialfinalproject.repository;

import com.example.financialfinalproject.domain.entity.DisLike;
import com.example.financialfinalproject.domain.entity.Post;
import com.example.financialfinalproject.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DisLikeRepository extends JpaRepository<DisLike, Integer> {

    Optional<DisLike> findByPostAndUser(Post post, User user);

    List<DisLike> findAllByPost(Post post);

    Long countByPost(Post post);

    @Transactional
    void deleteAllByPost(Post post);
}
