package com.bithumb.controller;

import com.bithumb.service.BoardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final BoardsService boardsService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("boards", boardsService.findAllDesc());
        return "index";
    }

    @GetMapping("/boards/write")
    public String boardWrite() {
        return "board-write";
    }
}
