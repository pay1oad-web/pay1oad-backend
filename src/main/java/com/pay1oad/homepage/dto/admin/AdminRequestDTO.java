package com.pay1oad.homepage.dto.admin;

import com.pay1oad.homepage.model.login.MemberAuth;
import lombok.*;

public class AdminRequestDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ToChangeMemberAuthDTO {
        @NonNull
        private Integer userId;
        @NonNull
        private MemberAuth memberAuth;
    }
}
