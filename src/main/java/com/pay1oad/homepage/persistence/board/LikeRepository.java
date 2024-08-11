package com.pay1oad.homepage.persistence.board;

import com.pay1oad.homepage.model.board.Like;
import com.pay1oad.homepage.model.board.Board;
import com.pay1oad.homepage.model.login.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long>{
    Optional<Like> findByBoardAndMember(Board board, Member member);
}
