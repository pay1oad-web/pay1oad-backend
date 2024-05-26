package com.pay1oad.homepage.service.board;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pay1oad.homepage.config.ResourceNotFoundException;
import com.pay1oad.homepage.dto.board.CommentDTO;
import com.pay1oad.homepage.dto.board.ResCommentDTO;
import com.pay1oad.homepage.model.board.Board;
import com.pay1oad.homepage.model.board.Comment;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.persistence.board.BoardRepository;
import com.pay1oad.homepage.persistence.board.CommentRepository;
import com.pay1oad.homepage.persistence.login.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Page<ResCommentDTO> getAllComments(Pageable pageable, Long boardId) {
        Page<Comment> comments = commentRepository.findAllWithMemberAndBoard(pageable, boardId);
        List<ResCommentDTO> commentList = comments.getContent().stream()
                .map(ResCommentDTO::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(commentList, pageable, comments.getTotalElements());
    }

    public ResCommentDTO write(Long boardId, Member member, CommentDTO writeDto) {
        // board 정보 검색
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ResourceNotFoundException("Board", "Board id", String.valueOf(boardId))
        );
        // member(댓글 작성자) 정보 검색
        Member commentWriter = memberRepository.findById(member.getUserid()).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Member id", String.valueOf(member.getUserid()))
        );
        // Entity 변환, 연관관계 매핑
        Comment comment = CommentDTO.ofEntity(writeDto);
        comment.setBoard(board);
        comment.setMember(commentWriter);

        Comment saveComment = commentRepository.save(comment);
        return ResCommentDTO.fromEntity(saveComment);
    }

    public ResCommentDTO update(Long commentId, CommentDTO commentDto) {
        Comment comment = commentRepository.findByIdWithMemberAndBoard(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "Comment Id", String.valueOf(commentId))
        );
        comment.update(commentDto.getContent());
        return ResCommentDTO.fromEntity(comment);
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}