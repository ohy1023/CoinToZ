package com.example.financialfinalproject.controller;

import com.example.financialfinalproject.domain.dto.CommentDto;
import com.example.financialfinalproject.domain.dto.ReplyCommentDto;
import com.example.financialfinalproject.domain.request.CommentCreateRequest;
import com.example.financialfinalproject.domain.request.CommentUpdateRequest;
import com.example.financialfinalproject.domain.request.ReplyCommentCreateRequest;
import com.example.financialfinalproject.domain.response.CommentResponse;
import com.example.financialfinalproject.domain.response.CommentUpdateResponse;
import com.example.financialfinalproject.domain.response.Response;
import com.example.financialfinalproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class CommentRestController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments") // 댓글 작성
    public ResponseEntity<Response<CommentDto>> createComment(@PathVariable Long postId, @RequestBody CommentCreateRequest commentCreateRequest, Authentication authentication) {
        CommentDto commentDto = commentService.createComment(postId, authentication.getName(), commentCreateRequest);
        return ResponseEntity.ok().body(Response.success(commentDto));
    }

    @PostMapping("/{postId}/comments/{commentId}") // 대댓글 작성
    public ResponseEntity<Response<ReplyCommentDto>> createReplyComment(@PathVariable Long postId, @PathVariable(name = "commentId") Integer parentId, @RequestBody ReplyCommentCreateRequest commentCreateRequest, Authentication authentication) {
        ReplyCommentDto replyCommentDto = commentService.createReplyComment(postId, authentication.getName(), commentCreateRequest,parentId);
        return ResponseEntity.ok().body(Response.success(replyCommentDto));
    }

    @GetMapping("{postId}/comments") // 댓글 목록 조회
    public ResponseEntity<Response<Page<CommentDto>>> getPostList(@PathVariable Long postId, @PageableDefault(size = 10)
    @SortDefault(sort = "registeredAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentDto> commentDtos = commentService.getAllItems(postId,pageable);
        return ResponseEntity.ok().body(Response.success(commentDtos));
    }

    @PutMapping("/{postId}/comments/{commentId}") // 댓글 수정
    public ResponseEntity<Response<CommentUpdateResponse>> updateComment(@PathVariable Long postId, @PathVariable Integer commentId, @RequestBody CommentUpdateRequest commentUpdateRequest, Authentication authentication) {
        CommentUpdateResponse commentUpdateResponse = commentService.updateComment(postId, commentId, commentUpdateRequest, authentication.getName());
        return ResponseEntity.accepted().body(Response.success(commentUpdateResponse));
    }

    @DeleteMapping("/{postId}/comments/{commentId}") // 댓글 삭제
    public ResponseEntity<Response<CommentResponse>> deleteComment(@PathVariable Long postId, @PathVariable Integer commentId, Authentication authentication) {
        commentService.deleteComment(postId, commentId, authentication.getName());
        return ResponseEntity.ok().body(Response.success(new CommentResponse("댓글 삭제 완료", commentId)));
    }
}
