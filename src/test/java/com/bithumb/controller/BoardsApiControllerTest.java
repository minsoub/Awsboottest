package com.bithumb.controller;

import com.bithumb.domain.board.Boards;
import com.bithumb.domain.board.BoardsRepository;
import com.bithumb.dto.BoardsSaveRequestDto;
import com.bithumb.dto.BoardsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@MockBean(JpaMetamodelMappingContext.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BoardsRepository boardsRepository;

    @After
    public void tearDown() throws Exception {
        boardsRepository.deleteAll();
    }

    @Test
    public void board_resgister() throws Exception {
        String title = "board title test";
        String content = "board content test";

        BoardsSaveRequestDto requestDto = BoardsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("minsoub@gmail.com")
                .build();
        String url = "http://localhost:"+port+"/api/v1/boards";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Boards> all = boardsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

    }

    @Test
    public void boards_update() throws Exception {
        Boards savedBoards = boardsRepository.save(Boards.builder()
                .title("board test title")
                .content("board content test")
                .author("minsoub@gmail.com")
                .build());

        Long updateId = savedBoards.getId();
        String expectedTitle = "test title2";
        String expectedContent = "test content2";

        BoardsUpdateRequestDto requestDto = BoardsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:"+port+"/api/v1/boards/"+updateId;
        HttpEntity<BoardsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Boards> all = boardsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}
