package com.pay1oad.homepage.dto.board;

import com.pay1oad.homepage.model.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardUpdateDTO {
    private String title;
    private String content;

    @Builder
    public BoardUpdateDTO(final Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
    }
}
