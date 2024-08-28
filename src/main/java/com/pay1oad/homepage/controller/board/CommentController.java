package com.pay1oad.homepage.controller.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pay1oad.homepage.dto.board.CommentDTO;
import com.pay1oad.homepage.dto.board.ResCommentDTO;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.service.board.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/board/{boardId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/list")
    public ResponseEntity<Page<ResCommentDTO>> commentList(
            @PathVariable("boardId") Long boardId,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ResCommentDTO> commentList = commentService.getAllComments(pageable, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    @PostMapping("/write")
    public ResponseEntity<ResCommentDTO> write(
            @AuthenticationPrincipal Member member,
            @PathVariable("boardId") Long boardId,
            @RequestBody CommentDTO commentDto) {

        ResCommentDTO saveCommentDTO = commentService.write(boardId, member, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveCommentDTO);
    }

    @PatchMapping("/update/{commentId}")
    public ResponseEntity<ResCommentDTO> update(
            @AuthenticationPrincipal Member member,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDTO commentDto) {

        ResCommentDTO updateCommentDTO = commentService.update(commentId, commentDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateCommentDTO);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Long> delete(
            @AuthenticationPrincipal Member member,
            @PathVariable("commentId") Long commentId) {

        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}