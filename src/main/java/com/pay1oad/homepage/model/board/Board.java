package com.pay1oad.homepage.model.board;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import com.pay1oad.homepage.model.login.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long board_id;

    @Column(nullable = false, length = 100)
    private String title;

    private String content;

    @Column(name = "view_count")
    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public List<FileEntity> files = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @Builder
    public Board(Long board_id, String title, String content, int viewCount, Member member, Category category) {
        this.board_id = board_id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.member = member;
        this.category = category;
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

    // Category & Board 연관관계 편의 메소드
    public void setCategory(Category category) {
        this.category = category;
        if (!category.getBoards().contains(this)) {
            category.getBoards().add(this);
        }
    }

    // Like
    public void addLike(Like like) {
        this.likes.add(like);
    }

    public void removeLike(Like like) {
        this.likes.remove(like);
    }

    public int getLikeCount() {
        return this.likes.size();
    }
}