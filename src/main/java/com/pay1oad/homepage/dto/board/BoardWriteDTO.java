package com.pay1oad.homepage.dto.board;

import com.pay1oad.homepage.model.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * -Request-
 * 게시글 등록 정보 요청, 작성자는 Authentication 받음
 */

@Getter
@Setter
@NoArgsConstructor
public class BoardWriteDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    public BoardWriteDTO(final Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
    }

    @Builder
    public static Board ofEntity(BoardWriteDTO dto) {
        return Board.builder()
                .title(dto.title)
                .content(dto.content)
                .build();
    }
}