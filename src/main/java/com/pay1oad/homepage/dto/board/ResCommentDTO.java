package com.pay1oad.homepage.dto.board;

import com.pay1oad.homepage.model.board.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * -Response- 댓글 등록, 수정 응답
 */

@Getter
@Setter
@NoArgsConstructor
public class ResCommentDTO {

	private Long commentId;
	private String content;
	private String createdDate;
	private String modifiedDate;
	private String commentWriterName; // 댓글 작성자

	@Builder
	public ResCommentDTO(Long commentId, String content, String createdDate, String modifiedDate,
			String commentWriterName) {
		this.commentId = commentId;
		this.content = content;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.commentWriterName = commentWriterName;
	}

	public static ResCommentDTO fromEntity(Comment comment) {
		return ResCommentDTO.builder().commentId(comment.getId()).content(comment.getContent())
				.createdDate(comment.getCreatedDate()).modifiedDate(comment.getModifiedDate())
				.commentWriterName(comment.getMember().getUsername()).build();
	}
}