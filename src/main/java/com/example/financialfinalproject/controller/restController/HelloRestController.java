package com.example.financialfinalproject.controller.restController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class HelloRestController {
    @GetMapping("/hello")
    public String test() {
        return "Hello, world!";
    }
}
