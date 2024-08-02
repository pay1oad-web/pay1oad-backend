package com.pay1oad.homepage.dto.board;

import com.pay1oad.homepage.model.board.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * -Request-
 * 댓글 등록, 수정 요청, <br>
 * -Member, Board는 URI Resource로 받음
 */

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {

    private String content;

    @Builder
    public CommentDTO(String content) {
        this.content = content;
    }

    public static Comment ofEntity(CommentDTO dto) {
        return Comment.builder()
                .content(dto.getContent())
                .build();
    }
}