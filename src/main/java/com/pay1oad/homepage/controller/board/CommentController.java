package com.pay1oad.homepage.controller.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pay1oad.homepage.dto.board.CommentDTO;
import com.pay1oad.homepage.dto.board.ResCommentDTO;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.service.login.MemberService;
import com.pay1oad.homepage.service.board.CommentService;
import com.pay1oad.homepage.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/board/{boardId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @GetMapping("/list")
    public ResponseEntity<Page<ResCommentDTO>> commentList(
            @PathVariable("boardId") Long boardId,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ResCommentDTO> commentList = commentService.getAllComments(pageable, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    @PostMapping("/write")
    public ResponseEntity<ResCommentDTO> write(
            HttpServletRequest request,
            @PathVariable("boardId") Long boardId,
            @RequestBody CommentDTO commentDto) {
        String token = request.getHeader("Authorization").substring(7);
        int userid = Integer.parseInt(tokenProvider.validateAndGetUserId(token));
        Member member = memberService.getMemberByID(userid);

        ResCommentDTO saveCommentDTO = commentService.write(boardId, member, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveCommentDTO);
    }

    @PatchMapping("/update/{commentId}")
    public ResponseEntity<ResCommentDTO> update(
            HttpServletRequest request,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDTO commentDto) {
        String token = request.getHeader("Authorization").substring(7);
        int userid = Integer.parseInt(tokenProvider.validateAndGetUserId(token));
        Member member = memberService.getMemberByID(userid);

        ResCommentDTO updateCommentDTO = commentService.update(commentId, commentDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateCommentDTO);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Long> delete(
            HttpServletRequest request,
            @PathVariable("commentId") Long commentId) {
        String token = request.getHeader("Authorization").substring(7);
        int userid = Integer.parseInt(tokenProvider.validateAndGetUserId(token));
        Member member = memberService.getMemberByID(userid);

        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}