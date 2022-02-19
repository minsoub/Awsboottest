package com.bithumb.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardsRepository extends JpaRepository<Boards, Long> {
    @Query("SELECT p FROM Boards p ORDER BY p.id DESC")
    List<Boards> findAllDesc();
}
