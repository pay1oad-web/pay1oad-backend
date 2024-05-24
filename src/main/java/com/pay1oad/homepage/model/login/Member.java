package com.pay1oad.homepage.model.login;

import com.pay1oad.homepage.model.board.Board;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;

    @Column(nullable = false)
    private String username;

    private String passwd;

    private String email;

    private String role;

    private String authProvider;

    private Boolean verified;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addBoard(Board board) {
        boards.add(board);
        board.setMember(this);
    }
}
