package com.pay1oad.homepage.controller.board;

import com.pay1oad.homepage.security.TokenProvider;
import com.pay1oad.homepage.service.board.BoardService;
import com.pay1oad.homepage.dto.board.BoardUpdateDTO;
import com.pay1oad.homepage.dto.board.BoardWriteDTO;
import com.pay1oad.homepage.dto.board.SearchDTO;
import com.pay1oad.homepage.dto.board.ResBoardDetailsDTO;
import com.pay1oad.homepage.dto.board.ResBoardListDTO;
import com.pay1oad.homepage.dto.board.ResBoardWriteDTO;
import com.pay1oad.homepage.model.login.Member;

import com.pay1oad.homepage.service.login.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberService memberService;

    // 페이징 목록
    @GetMapping("/list")
    public ResponseEntity<Page<ResBoardListDTO>> boardList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ResBoardListDTO> listDTO = boardService.getAllBoards(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }

    // 페이징 검색, Get 요청 @RequestBody 사용할 수 없음
    @PostMapping("/search")
    public ResponseEntity<Page<ResBoardListDTO>> search(
            @RequestBody SearchDTO searchDTO,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal Member member) {
        // 로그인한 사용자가 검색 조건을 제공하지 않았을 경우, 자신의 정보로 채움
        if (searchDTO.getUsername() == null || searchDTO.getUsername().isEmpty()) {
            searchDTO.setUsername(member.getUsername());
        }
        Page<ResBoardListDTO> searchResults = boardService.search(searchDTO, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(searchResults);
    }

    @PostMapping("/write")
    public ResponseEntity<ResBoardWriteDTO> write(
            @RequestBody BoardWriteDTO boardDTO,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        int userid= Integer.parseInt(tokenProvider.validateAndGetUserId(token));
        Member member=memberService.getMemberByID(userid);

        ResBoardWriteDTO saveBoardDTO = boardService.write(boardDTO, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveBoardDTO);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ResBoardDetailsDTO> detail(@PathVariable("boardId") Long boardId) {
        ResBoardDetailsDTO findBoardDTO = boardService.detail(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(findBoardDTO);
    }

    // 상세보기 -> 수정
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
    @DeleteMapping("/{boardId}/delete")
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
}
