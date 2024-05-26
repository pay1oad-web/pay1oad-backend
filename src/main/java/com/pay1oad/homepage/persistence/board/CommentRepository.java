package com.pay1oad.homepage.persistence.board;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pay1oad.homepage.model.board.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT c FROM Comment c JOIN FETCH c.member JOIN FETCH c.board b WHERE b.id = :boardId")
    Page<Comment> findAllWithMemberAndBoard(Pageable pageable, @Param("boardId") Long boardId);

    @Query(value = "SELECT c FROM Comment c JOIN FETCH c.member m JOIN FETCH c.board b WHERE c.id = :commentId")
    Optional<Comment> findByIdWithMemberAndBoard(@Param("commentId") Long commentId);
}