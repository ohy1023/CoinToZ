package com.example.financialfinalproject.controller.restController;

import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.exception.ErrorCode;
import com.example.financialfinalproject.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/emails")
@RequiredArgsConstructor
@Slf4j
public class EmailRestController {
    private final EmailService emailService;

    // 로그인 시 인증 이메일 보내기
    @ResponseBody
    @GetMapping("/send")
    public String sendAuthEmail(@RequestParam String email) throws Exception {
        return emailService.sendLoginAuthMessage(email);
    }

    // 이메일 인증 번호 확인하기
    @ResponseBody
    @GetMapping("/auth")
    public ResponseEntity checkAuthEmail(@RequestParam String code) {
        System.out.println(code);
            if (emailService.getData(code) == null) {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            } else return new ResponseEntity<>(true, HttpStatus.OK);
    }
}