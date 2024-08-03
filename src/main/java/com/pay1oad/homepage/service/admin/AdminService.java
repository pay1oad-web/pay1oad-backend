package com.pay1oad.homepage.service.admin;

import com.pay1oad.homepage.dto.admin.AdminRequestDTO;
import com.pay1oad.homepage.exception.CustomException;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.model.login.MemberAuth;
import com.pay1oad.homepage.persistence.login.MemberRepository;
import com.pay1oad.homepage.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    public void changeMemberAuth(AdminRequestDTO.ToChangeMemberAuthDTO toChangeMemberAuthDTO){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Member toChangeAuth = memberRepository.findByUserid(toChangeMemberAuthDTO.getUserId());

        boolean hasAdminRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        boolean hasOperatorRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_OPERATOR"));

        if (!(hasAdminRole || hasOperatorRole)) {
            throw new CustomException(ErrorStatus._UNAUTHORIZED);
        } else if (hasOperatorRole && (toChangeMemberAuthDTO.getMemberAuth().equals(MemberAuth.ADMIN) || toChangeAuth.getMemberAuth()==MemberAuth.ADMIN)) {
            throw new CustomException(ErrorStatus._OPERATORAUTHORIZED);
        }

        memberRepository.updateMemberAuth(toChangeMemberAuthDTO.getUserId(), toChangeMemberAuthDTO.getMemberAuth());
    }

}
