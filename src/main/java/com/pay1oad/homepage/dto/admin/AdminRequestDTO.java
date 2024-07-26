package com.pay1oad.homepage.dto.admin;

import com.pay1oad.homepage.model.login.MemberAuth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminRequestDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToChangeMemberAuthDTO {
        private Integer userId;
        private MemberAuth memberAuth;
    }
}
