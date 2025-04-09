package com.example.financialfinalproject.controller;

import com.example.financialfinalproject.domain.dto.PostDetailDto;
import com.example.financialfinalproject.domain.dto.PostDto;
import com.example.financialfinalproject.domain.request.UserPostEditRequest;
import com.example.financialfinalproject.domain.request.UserPostRequest;
import com.example.financialfinalproject.domain.response.*;
import com.example.financialfinalproject.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @PostMapping("")
    public Response<UserPostResponse> write(@RequestBody UserPostRequest userPostRequest, @ApiIgnore Authentication authentication) {
        String name = authentication.getName();
        PostDto postDto = postService.write(userPostRequest, name);
        return Response.success(new UserPostResponse("포스트 등록 완료", postDto.getId()));
    }

    @GetMapping("/{postId}")
    public Response<UserPostDetailResponse> detail(@PathVariable Long postId) {
        PostDetailDto postDetailDto = postService.detail(postId);
        return Response.success(new UserPostDetailResponse(postDetailDto.getId(), postDetailDto.getTitle(),
                postDetailDto.getBody(), postDetailDto.getUserName(),
                postDetailDto.getLikeCount(),postDetailDto.getDisLikeCount(),
                postDetailDto.getCreatedAt(), postDetailDto.getLastModifiedAt()));

    }

    @GetMapping("")
    public Response<List<UserPostDetailResponse>> list() {
        List<UserPostDetailResponse> list = postService.list();
        return Response.success(list);
    }

    @ApiOperation(value = "내가 작성한 포스트 목록")
    @GetMapping("/myPost")
    public Response<List<UserPostDetailResponse>> getMyPost(Authentication authentication) {
        String email = authentication.getName();
        List<UserPostDetailResponse> list = postService.getMyPosts(email);
        return Response.success(list);
    }


    @DeleteMapping("/{id}")
    public Response<UserPostDeleteResponse> delete(@PathVariable Long id, @ApiIgnore Authentication authentication) {

        String name = authentication.getName();
        PostDto postDto = postService.delete(id, name);
        return Response.success(new UserPostDeleteResponse("포스트 삭제 완료", id));

    }

    @PutMapping("/{id}")
    public Response<UserPostEditResponse> edit(@PathVariable Long id, @ApiIgnore Authentication authentication, @RequestBody UserPostEditRequest userPostEditRequest) {
        String name = authentication.getName();
        PostDto postDto = postService.edit(id, name, userPostEditRequest);
        return Response.success(new UserPostEditResponse("포스트 수정 완료", postDto.getId()));

    }


}
