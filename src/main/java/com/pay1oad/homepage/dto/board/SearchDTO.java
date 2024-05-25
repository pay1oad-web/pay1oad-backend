package com.pay1oad.homepage.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDTO {
    String title;
    String content;
    String username;

    public static SearchDTO createdSearchData(String title, String content, String username) {
        return SearchDTO.builder()
                .title(title)
                .content(content)
                .username(username)
                .build();
    }
}
