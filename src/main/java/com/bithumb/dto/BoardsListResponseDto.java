package com.bithumb.dto;

import com.bithumb.domain.board.Boards;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardsListResponseDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;

    public BoardsListResponseDto(Boards entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
    }
}
