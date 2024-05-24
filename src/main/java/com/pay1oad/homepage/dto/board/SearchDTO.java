package com.pay1oad.homepage.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDTO {
    String title;
    String content;
    String username;

    @Builder
    public SearchDTO(String title, String content, String username) {
        this.title = title;
        this.content = content;
        this.username = username;
    }

    public static SearchDTO createdSearchData(String title, String content, String username) {
        return SearchDTO.builder()
                .title(title)
                .content(content)
                .username(username)
                .build();
    }
}
