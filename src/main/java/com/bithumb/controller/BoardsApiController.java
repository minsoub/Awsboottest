package com.bithumb.controller;

import com.bithumb.dto.BoardsResponseDto;
import com.bithumb.dto.BoardsSaveRequestDto;
import com.bithumb.dto.BoardsUpdateRequestDto;
import com.bithumb.service.BoardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BoardsApiController {

    private final BoardsService boardsService;

    @PostMapping("/api/v1/boards")
    public Long save(@RequestBody BoardsSaveRequestDto requestDto) {
        return boardsService.save(requestDto);
    }

    @PutMapping("/api/v1/boards/{id}")
    public Long update(@PathVariable Long id, @RequestBody BoardsUpdateRequestDto requestDto)
    {
        return boardsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/boards/{id}")
    public BoardsResponseDto findById(@PathVariable Long id)
    {
        return boardsService.findById(id);
    }
}
