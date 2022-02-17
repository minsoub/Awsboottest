package com.bithumb.domain.boards;

import com.bithumb.domain.board.Boards;
import com.bithumb.domain.board.BoardsRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardsRepositoryTest {
    @Autowired
    BoardsRepository boardsRepository;

    @After
    public void cleanup() {
        boardsRepository.deleteAll();
    }

    @Test
    public void board_read() {
        String title = "일반적인 테스트 글 타이틀";
        String content = "게시글 내용";

        boardsRepository.save(Boards.builder()
                .title(title)
                .content(content)
                .author("minsoub@gmail.com")
                .build());

        // when
        List<Boards> boardsList = boardsRepository.findAll();

        // then
        Boards boards = boardsList.get(0);
        assertThat(boards.getTitle()).isEqualTo(title);
        assertThat(boards.getContent()).isEqualTo(content);
    }
}
