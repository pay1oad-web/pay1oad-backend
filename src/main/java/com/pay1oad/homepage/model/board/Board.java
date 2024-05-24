package com.pay1oad.homepage.model.board;

import com.pay1oad.homepage.model.login.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "BOARDID")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(name = "VIEW_COUNT")
    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private Member member;

    /*@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public List<FileEntity> files = new ArrayList<>();*/

    @Builder
    public Board(Long id, String title, String content, int viewCount, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.member = member;
    }

    // 조회수 증가
    public void upViewCount() {
        this.viewCount++;
    }

    // 수정 Dirty Checking
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Member & Board 연관관계 편의 메소드
    public void setMember(Member member) {
        this.member = member;
        if (!member.getBoards().contains(this)) {
            member.getBoards().add(this);
        }
    }
}
