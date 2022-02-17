package com.bithumb.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardsRepository extends JpaRepository<Boards, Long> {
}
