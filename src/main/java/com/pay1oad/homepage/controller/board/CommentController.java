/*
package com.pay1oad.homepage.controller.board;

import com.pay1oad.homepage.dto.board.CommentDTO;
import com.pay1oad.homepage.dto.board.ResCommentDTO;
import com.pay1oad.homepage.service.board.CommentService;
import com.pay1oad.homepage.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/{boardId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/list")
    public ResponseEntity<Page<ResCommentDTO>> commentList(
            @PathVariable Long boardId,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ResCommentDTO> commentList = commentService.getAllComments(pageable, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    @PostMapping("/write")
    public ResponseEntity<ResCommentDTO> write(
            @AuthenticationPrincipal Member member,
            @PathVariable Long boardId,
            @RequestBody CommentDTO commentDto) {

        ResCommentDTO saveCommentDTO = commentService.write(boardId, member.getUsername(), commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveCommentDTO);
    }

    @PatchMapping("/update/{commentId}")
    public ResponseEntity<ResCommentDTO> update(
            @AuthenticationPrincipal Member member,
            @PathVariable Long commentId,
            @RequestBody CommentDTO commentDto) {

        ResCommentDTO updateCommentDTO = commentService.update(commentId, member.getUsername(), commentDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateCommentDTO);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Long> delete(
            @AuthenticationPrincipal Member member,
            @PathVariable Long commentId) {

        commentService.delete(commentId, member.getUsername());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
*/
