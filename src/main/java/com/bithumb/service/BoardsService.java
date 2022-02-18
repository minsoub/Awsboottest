package com.bithumb.service;

import com.bithumb.domain.board.Boards;
import com.bithumb.domain.board.BoardsRepository;
import com.bithumb.dto.BoardsResponseDto;
import com.bithumb.dto.BoardsSaveRequestDto;
import com.bithumb.dto.BoardsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class BoardsService {
    private final BoardsRepository boardsRepository;

    @Transactional
    public Long save(BoardsSaveRequestDto requestDto) {
        return boardsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, BoardsUpdateRequestDto requestDto) {
        Boards boards = boardsRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 글이 없습니다. id="+id));

        boards.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public BoardsResponseDto findById(Long id) {
        Boards entity = boardsRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 글이 없습니다. id="+id));

        return new BoardsResponseDto(entity);
    }
}
