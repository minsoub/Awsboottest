package com.bithumb.dto;

import com.bithumb.domain.board.Boards;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardsUpdateRequestDto {
    private String content;
    private String title;

    @Builder
    public BoardsUpdateRequestDto(String title, String content) {
        this.content = content;
        this.title = title;
    }
}
