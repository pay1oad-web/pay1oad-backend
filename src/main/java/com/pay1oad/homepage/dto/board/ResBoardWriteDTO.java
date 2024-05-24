package com.pay1oad.homepage.dto.board;

import com.pay1oad.homepage.model.board.Board;
import com.pay1oad.homepage.model.login.Member; // 추가된 import문
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResBoardWriteDTO {
    private Long boardId;
    private String title;
    private String content;
    private String username;
    private String createdDate;

    @Builder
    public ResBoardWriteDTO(Long boardId, String title, String content, String username, String createdDate) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.username = username;
        this.createdDate = createdDate;
    }

    public static ResBoardWriteDTO fromEntity(Board board, Member member) {
        return ResBoardWriteDTO.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .username(member.getUsername())
                .createdDate(board.getCreatedDate())
                .build();
    }
}
