package com.pay1oad.homepage.dto.board;

import com.pay1oad.homepage.model.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * -Request-
 * 게시글 등록 정보 요청, 작성자는 Authentication 받음
 */

@Getter
@Setter
@NoArgsConstructor
public class BoardWriteDTO {

    private String title;
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
