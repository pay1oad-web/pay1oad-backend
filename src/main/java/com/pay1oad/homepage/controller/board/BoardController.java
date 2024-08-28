package com.pay1oad.homepage.controller.board;

import com.pay1oad.homepage.dto.board.*;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.security.TokenProvider;
import com.pay1oad.homepage.service.board.BoardService;
import com.pay1oad.homepage.service.login.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    // 페이징 목록
    @GetMapping("/list")
    public ResponseEntity<Page<ResBoardListDTO>> boardList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ResBoardListDTO> listDTO = boardService.getAllBoards(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }

    // 페이징 검색
    @PostMapping("/search")
    public ResponseEntity<Page<ResBoardListDTO>> search(
            @RequestBody SearchDTO searchDTO,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal Member member) {
        if (searchDTO.getUsername() == null || searchDTO.getUsername().isEmpty()) {
            searchDTO.setUsername(member.getUsername());
        }
        Page<ResBoardListDTO> searchResults = boardService.search(searchDTO, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(searchResults);
    }

    @PostMapping("/{categoryId}/write")
    public ResponseEntity<ResBoardWriteDTO> write(
            @RequestBody BoardWriteDTO boardDTO,
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long categoryId) {
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        int userid = Integer.parseInt(tokenProvider.validateAndGetUserId(token));
        Member member = memberService.getMemberByID(userid);

        ResBoardWriteDTO saveBoardDTO = boardService.write(boardDTO, member, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveBoardDTO);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ResBoardDetailsDTO> detail(@PathVariable("boardId") Long boardId) {
        ResBoardDetailsDTO findBoardDTO = boardService.detail(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(findBoardDTO);
    }

    // 상세보기
    @PatchMapping("/{boardId}/update")
    public ResponseEntity<ResBoardDetailsDTO> update(
            @PathVariable("boardId") Long boardId,
            @RequestBody BoardUpdateDTO boardDTO,
            @AuthenticationPrincipal Member member) {
        if (!boardService.checkOwnership(boardId, member.getUsername())) {
            log.warn("권한 거부: 사용자 {}는 게시글 {}을 수정할 권한이 없습니다.", member.getUsername(), boardId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ResBoardDetailsDTO updatedBoard = boardService.update(boardId, boardDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBoard);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> delete(
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal Member member) {
        // 권한 검증 로직
        if (!boardService.checkOwnership(boardId, member.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        boardService.delete(boardId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 카테고리별 게시글 조회
    @GetMapping("/category/{categoryId}/list")
    public ResponseEntity<Page<ResBoardListDTO>> getBoardsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ResBoardListDTO> boards = boardService.getBoardsByCategory(categoryId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(boards);
    }

    // 좋아요 토글 기능
    @PostMapping("/{boardId}/like")
    public ResponseEntity<Void> toggleLike(
            @PathVariable Long boardId,
            @RequestHeader("Authorization") String authorizationHeader){
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        int userid = Integer.parseInt(tokenProvider.validateAndGetUserId(token));
        Member member = memberService.getMemberByID(userid);
        boardService.toggleLike(boardId, member);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 좋아요 개수 조회
    @GetMapping("/{boardId}/like-count")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long boardId) {
        int likeCount = boardService.getLikeCount(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(likeCount);
    }
}