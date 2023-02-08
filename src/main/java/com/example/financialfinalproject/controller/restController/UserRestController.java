package com.example.financialfinalproject.controller.restController;

import com.example.financialfinalproject.domain.dto.UserDto;
import com.example.financialfinalproject.domain.request.*;
import com.example.financialfinalproject.domain.response.*;
import com.example.financialfinalproject.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserRestController {
    private final UserService userService;

    @ApiOperation(value = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<Response<UserJoinResponse>> joinUser(@RequestBody UserJoinRequest userJoinRequest) {
        UserJoinResponse userJoinResponse = userService.join(userJoinRequest);

        return ResponseEntity.ok().body(Response.success(userJoinResponse));
    }




    @ApiOperation(value = "로그인", notes = "jwt 반환")
    @PostMapping("/login")
    public String login(@RequestBody UserLoginRequest userLoginRequest) {
        return "ok";
    }

    @ApiOperation(value = "유저 정보 조회")
    @GetMapping("/info")
    public ResponseEntity<Response<UserGetResponse>> getUserInfo(Authentication authentication) {
        String email = authentication.getName();
        log.info("userEmail:{}", email);
        UserGetResponse info = userService.getInfo(email);
        return ResponseEntity.ok().body(Response.success(info));
    }

    @ApiOperation(value = "비밀번호 일치 여부 검증")
    @PostMapping("/password")
    public ResponseEntity<Response<Boolean>> validatePassword(@RequestBody UserPasswordRequest request, Authentication authentication) {
        String email = authentication.getName();
        log.info("userEmail:{}", email);
        String password = request.getPassword();
        boolean validation = userService.validate(email, password);
        return ResponseEntity.ok().body(Response.success(validation));
    }

    @ApiOperation(value = "비밀번호 변경")
    @PutMapping("/password")
    public ResponseEntity<Response<String>> updatePassword(@RequestBody UserUpdatePasswordRequest request, Authentication authentication) {
        String email = authentication.getName();
        log.info("userEmail:{}", email);
        Integer userId = userService.modifyPassword(email, request);
        return ResponseEntity.ok().body(Response.success(" 비밀번호가 변경되었습니다.\n 다시 로그인해 주세요."));
    }

    @ApiOperation(value = "유저 정보 수정")
    @PostMapping("/modify")
    public ResponseEntity<Response<UserPutResponse>> modifyUser(MultipartFile image ,String userName, int removeClick, Authentication authentication) throws IOException {
        String email = authentication.getName();
        log.info("image:{}",image);
        log.info("userName:{}",userName);
        log.info("userEmail:{}", email);
        UserPutResponse response = userService.modify(image,userName,email,removeClick);
        return ResponseEntity.ok().body(Response.success(response));
    }

    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping
    public ResponseEntity<Response<String>> deleteUser(Authentication authentication) {
        String email = authentication.getName();
        log.info("userEmail:{}", email);
        Integer userId = userService.delete(email);
        return ResponseEntity.ok().body(Response.success("회원이 탈퇴되었습니다."));
    }

    @ApiOperation(value = "역할 변경")
    @PostMapping("/{userId}/role")
    public ResponseEntity<Response<UserRoleResponse>> changeRole(@PathVariable Integer userId, Authentication authentication) {
        UserRoleResponse response = userService.changeRole(userId, authentication.getName());
        return ResponseEntity.ok().body(Response.success(response));
    }

    @GetMapping("/reissuance")
    public ResponseEntity<String> reissue() {
        return ResponseEntity.ok().body("SUCCESS");
    }

    @GetMapping("/test")
    public String test() {
        return "SUCCESS";
    }


}
