package com.bithumb.domain.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;

@Getter
@NoArgsConstructor
@Entity
public class Boards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto_increment
    private Long id;

    @Column(length = 500, nullable = false)  // default : 255
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    public Boards(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    /**
     * JPS의 영속성 컨텍스트 이므로 데이터를 저장해도 엔티티를 영구 저장한다.
     * @param title
     * @param content
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }


}
