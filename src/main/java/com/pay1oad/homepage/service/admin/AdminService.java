package com.pay1oad.homepage.service.admin;

import com.pay1oad.homepage.dto.admin.AdminRequestDTO;
import com.pay1oad.homepage.persistence.login.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    public void changeMemberAuth(AdminRequestDTO.ToChangeMemberAuthDTO toChangeMemberAuthDTO){

        memberRepository.updateMemberAuth(toChangeMemberAuthDTO.getUserId(), toChangeMemberAuthDTO.getMemberAuth());
    }

}
