package com.example.financialfinalproject.controller.restController;

import com.example.financialfinalproject.domain.dto.UpbitTokenDto;
import com.example.financialfinalproject.domain.response.Response;
import com.example.financialfinalproject.domain.response.UserGetResponse;
import com.example.financialfinalproject.repository.UserRepository;
import com.example.financialfinalproject.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.http.HttpHeaders;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UpbitUserController {

private final UserService userService;
@PostMapping("/UpbitToken")
@ApiOperation(value = "업비트 API key 저장", notes = "업비트 API key 저장")
public void save(@RequestBody UpbitTokenDto upbitTokenDto, @ApiIgnore Authentication authentication){
   String email =  authentication.getName(); // email 추출
   userService.save(upbitTokenDto,email);
}

}
