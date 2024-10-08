package com.pay1oad.homepage.service.board;

import com.pay1oad.homepage.config.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import com.pay1oad.homepage.model.board.Board;
import com.pay1oad.homepage.model.board.Like;
import com.pay1oad.homepage.persistence.board.BoardRepository;
import com.pay1oad.homepage.persistence.board.LikeRepository;
import com.pay1oad.homepage.dto.board.BoardUpdateDTO;
import com.pay1oad.homepage.dto.board.BoardWriteDTO;
import com.pay1oad.homepage.dto.board.SearchDTO;
import com.pay1oad.homepage.dto.board.ResBoardDetailsDTO;
import com.pay1oad.homepage.dto.board.ResBoardListDTO;
import com.pay1oad.homepage.dto.board.ResBoardWriteDTO;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.persistence.login.MemberRepository;
import com.pay1oad.homepage.model.board.Category;
import com.pay1oad.homepage.persistence.board.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SpringBootApplication
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;  // 추가: LikeRepository 주입
    private final CategoryRepository categoryRepository;

    // 페이징 리스트
    public Page<ResBoardListDTO> getAllBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllWithMemberAndComments(pageable);
        List<ResBoardListDTO> list = boards.getContent().stream()
                .map(ResBoardListDTO::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, boards.getTotalElements());
    }

    public Page<ResBoardListDTO> search(SearchDTO searchData, Pageable pageable) {
        Page<Board> result = null;

        // 제목과 내용을 모두 포함하여 검색
        if (searchData.getTitle() != null && !searchData.getTitle().isEmpty()) {
            result = boardRepository.findAllTitleContaining(searchData.getTitle(), pageable);
        }
        // 내용만 포함하여 검색
        else if (searchData.getContent() != null && !searchData.getContent().isEmpty()) {
            result = boardRepository.findAllContentContaining(searchData.getContent(), pageable);
        }
        // 사용자 이름만 포함하여 검색
        else if (searchData.getUsername() != null && !searchData.getUsername().isEmpty()) {
            result = boardRepository.findAllUsernameContaining(searchData.getUsername(), pageable);
        }

        if (result != null) {
            List<ResBoardListDTO> list = result.getContent().stream()
                    .map(ResBoardListDTO::fromEntity)
                    .collect(Collectors.toList());
            return new PageImpl<>(list, pageable, result.getTotalElements());
        }

        // 기본적으로 빈 결과 반환
        return Page.empty(pageable);
    }

    //

    public ResBoardWriteDTO write(BoardWriteDTO boardDTO, Member member) {
        Board board = BoardWriteDTO.ofEntity(boardDTO);  // 'ofEntity' 사용
        board.setMember(member);
        Board savedBoard = boardRepository.save(board);
        return ResBoardWriteDTO.fromEntity(savedBoard, member);  // Member 객체 전달
    }

    // 게시글 상세보기
    public ResBoardDetailsDTO detail(Long boardId) {
        Board findBoard = boardRepository.findByIdWithMemberAndCommentsAndFiles(boardId).orElseThrow(
                () -> new ResourceNotFoundException("Board", "Board Id", String.valueOf(boardId))
        );
        // 조회수 증가
        findBoard.upViewCount();
        return ResBoardDetailsDTO.fromEntity(findBoard);
    }

    // 게시글 수정
    public ResBoardDetailsDTO update(Long boardId, BoardUpdateDTO boardDTO) {
        Board updateBoard = boardRepository.findByIdWithMemberAndCommentsAndFiles(boardId).orElseThrow(
                () -> new ResourceNotFoundException("Board", "Board Id", String.valueOf(boardId))
        );
        updateBoard.update(boardDTO.getTitle(), boardDTO.getContent());
        return ResBoardDetailsDTO.fromEntity(updateBoard);
    }

    // 게시글 삭제
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    public boolean checkOwnership(Long boardId, String username) {
        return boardRepository.findById(boardId)
                .map(board -> board.getMember().getUsername().equals(username))
                .orElse(false);
    }

    // 카테고리별 게시판 조회 기능
    public Page<ResBoardListDTO> getBoardsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "Category Id", String.valueOf(categoryId))
        );

        Page<Board> boards = boardRepository.findByCategory(category, pageable);
        List<ResBoardListDTO> list = boards.getContent().stream()
                .map(ResBoardListDTO::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, boards.getTotalElements());
    }

    // 카테고리 지정
    public ResBoardWriteDTO write(BoardWriteDTO boardDTO, Member member, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "Category Id", String.valueOf(categoryId))
        );

        Board board = BoardWriteDTO.ofEntity(boardDTO);
        board.setMember(member);
        board.setCategory(category);  // 카테고리 설정
        Board savedBoard = boardRepository.save(board);
        return ResBoardWriteDTO.fromEntity(savedBoard, member);
    }

    // 좋아요 토글 기능
    public void toggleLike(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ResourceNotFoundException("Board", "Board Id", String.valueOf(boardId))
        );
        Like existingLike  = likeRepository.findByBoardAndMember(board, member).orElse(null);
        if (existingLike != null) {
            board.removeLike(existingLike);
            likeRepository.delete(existingLike);
        } else {
            Like like = new Like(board, member);
            likeRepository.save(like);
            board.addLike(like);
        }
    }

    // 좋아요 개수 조회
    public int getLikeCount(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ResourceNotFoundException("Board", "Board Id", String.valueOf(boardId))
        );
        return board.getLikeCount();
    }
}