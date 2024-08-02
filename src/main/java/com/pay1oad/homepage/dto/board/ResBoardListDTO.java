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
public class ResBoardListDTO {
    private Long boardId;
    private String title;
    private String content;
    private int viewCount;
    private String createdDate;
    private String modifiedDate;
    private String username;

    @Builder
    public ResBoardListDTO(Long boardId, String title, String content, int viewCount, String createdDate, String modifiedDate, String username) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.username = username;
    }

    // Entity -> DTO
    public static ResBoardListDTO fromEntity(Board board) {
        Member member = board.getMember(); // Member 객체 가져오기
        return ResBoardListDTO.builder()
                .boardId(board.getBoard_id())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .username(member.getUsername()) // Member 객체에서 username 가져오기
                .build();
    }
}
