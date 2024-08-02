package com.pay1oad.homepage.service.login;

import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.persistence.login.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                member.getUsername(),
                member.getPasswd(),
                getAuthorities(member)
        );
    }


    private Collection<? extends GrantedAuthority> getAuthorities(Member member) {
        // MemberAuth를 기반으로 권한 설정
        log.info(member.getMemberAuth().name());
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getMemberAuth().name()));
    }
}
