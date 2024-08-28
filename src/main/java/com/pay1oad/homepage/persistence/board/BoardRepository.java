package com.pay1oad.homepage.persistence.board;

import java.util.List;
import java.util.Optional;

import com.pay1oad.homepage.model.board.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pay1oad.homepage.model.board.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

	@Query(value = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.board_id = :boardID")
	Optional<Board> findByIdWithMemberAndCommentsAndFiles(@Param("boardID") Long boardID);

	// 첫 페이징 화면("/")
	@Query(value = "SELECT b FROM Board b JOIN FETCH b.member")
	Page<Board> findAllWithMemberAndComments(Pageable pageable);

	// 제목 검색
	@Query(value = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.title LIKE %:title%")
	Page<Board> findAllTitleContaining(@Param("title") String title, Pageable pageable);

	// 내용 검색
	@Query(value = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.content LIKE %:content%")
	Page<Board> findAllContentContaining(@Param("content") String content, Pageable pageable);

	// 작성자 검색
	@Query(value = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.member.username LIKE %:username%")
	Page<Board> findAllUsernameContaining(@Param("username") String username, Pageable pageable);

	// 카테고리별 게시글 조회
	Page<Board> findByCategory(Category category, Pageable pageable);
}