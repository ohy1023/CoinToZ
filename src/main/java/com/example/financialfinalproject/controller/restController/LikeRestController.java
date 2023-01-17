package com.example.financialfinalproject.controller.restController;

import com.example.financialfinalproject.domain.response.Response;
import com.example.financialfinalproject.service.LikeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class LikeRestController {

    private final LikeService likeService;

    @PostMapping("/{postId}/likes") // 좋아요 누르기
    public ResponseEntity<Response<String>> addLikeCount(@PathVariable Long postId, Authentication authentication) {
        likeService.addLikeCount(postId, authentication.getName());
        return ResponseEntity.ok().body(Response.success("좋아요를 눌렀습니다."));
    }

    @DeleteMapping("/{postId}/likes") // 좋아요 삭제
    public ResponseEntity<Response<String>> deleteLikeCount(@PathVariable Long postId, Authentication authentication) {
        likeService.deleteLikeCount(postId, authentication.getName());
        return ResponseEntity.ok().body(Response.success("좋아요를 취소했습니다."));
    }

    @GetMapping("/{postId}/likes") // 좋아요 수 조회
    public ResponseEntity<Response<Long>> viewLikeCount(@PathVariable Long postId) {
        Long likeCnt = likeService.viewLikeCount(postId);
        return ResponseEntity.ok().body(Response.success(likeCnt));
    }

    @PostMapping("/{postId}/Dislikes") // 싫어요 누르기
    public ResponseEntity<Response<String>> addDisLikeCount(@PathVariable Long postId, Authentication authentication) {
        likeService.addDisLikeCount(postId, authentication.getName());
        return ResponseEntity.ok().body(Response.success("싫어요를 눌렀습니다."));
    }

    @DeleteMapping("/{postId}/Dislikes") // 싫어요 삭제
    public ResponseEntity<Response<String>> deleteDisLikeCount(@PathVariable Long postId, Authentication authentication) {
        likeService.deleteDisLikeCount(postId, authentication.getName());
        return ResponseEntity.ok().body(Response.success("싫어요를 취소했습니다."));
    }

    @GetMapping("/{postId}/Dislikes") // 싫어요 수 조회
    public ResponseEntity<Response<Long>> viewDisLikeCount(@PathVariable Long postId) {
        Long DislikeCnt = likeService.viewDisLikeCount(postId);
        return ResponseEntity.ok().body(Response.success(DislikeCnt));
    }
}
