package com.pay1oad.homepage.dto.board;

import com.pay1oad.homepage.model.board.Board;
import com.pay1oad.homepage.model.login.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ResBoardDetailsDTO {
    private Long boardId;
    private String title;
    private String content;
    private int viewCount;
    private String username;
    private String createdDate;
    private String modifiedDate;

    @Builder
    public ResBoardDetailsDTO(Long boardId, String title, String content, int viewCount, String username, String createdDate, String modifiedDate/*List<ResCommentDTO> comments, List<ResBoardDetailsFileDTO> files*/) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.username = username;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        /*this.comments = comments;
        this.files = files;*/
    }

    public static ResBoardDetailsDTO fromEntity(Board board) {
        Member member = board.getMember(); // Member 객체 가져오기
        return ResBoardDetailsDTO.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .username(member.getUsername()) // Member 객체에서 username 가져오기
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                /*.comments(board.getComments().stream()
                        .map(ResCommentDTO::fromEntity)
                        .collect(Collectors.toList()))
                .files(board.getFiles().stream()
                        .map(ResBoardDetailsFileDTO::fromEntity)
                        .collect(Collectors.toList()))*/
                .build();
    }
}
