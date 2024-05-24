package com.pay1oad.homepage.persistence.login;


import com.pay1oad.homepage.model.login.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByUsername(String username);
    Boolean existsByUsername(String username);
    Member findByUsernameAndPasswd(String username, String passwd);
    Member findByUserid(Integer Userid);
}
