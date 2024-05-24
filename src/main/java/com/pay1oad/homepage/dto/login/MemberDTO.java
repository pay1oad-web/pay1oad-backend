package com.pay1oad.homepage.dto.login;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String token;
    private String username;
    private String passwd;
    private String userid;
    private String email;
}
