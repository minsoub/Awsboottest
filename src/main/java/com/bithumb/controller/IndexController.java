package com.bithumb.controller;

import com.bithumb.config.auth.dto.SessionUser;
import com.bithumb.dto.BoardsResponseDto;
import com.bithumb.service.BoardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final BoardsService boardsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("boards", boardsService.findAllDesc());

        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/boards/write")
    public String boardWrite() {
        return "board-write";
    }

    @GetMapping("/boards/update/{id}")
    public String boardsUpdate(@PathVariable Long id, Model model) {
        BoardsResponseDto dto = boardsService.findById(id);
        model.addAttribute("boards", dto);

        return "board-update";
    }
}
