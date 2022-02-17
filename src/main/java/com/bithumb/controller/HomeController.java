package com.bithumb.controller;

import com.bithumb.dto.HomeResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/home")
    public String home() {
        return "Home";
    }

    @GetMapping("/home/name")
    public HomeResponseDto getName(@RequestParam("name") String name, @RequestParam("amount") int amount) {
        return new HomeResponseDto(name, amount);
    }
}
