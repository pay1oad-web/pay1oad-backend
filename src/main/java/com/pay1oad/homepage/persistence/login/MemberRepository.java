package com.pay1oad.homepage.persistence.login;


import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.model.login.MemberAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByUsername(String username);
    Boolean existsByUsername(String username);
    Member findByUsernameAndPasswd(String username, String passwd);
    Member findByUserid(Integer Userid);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.memberAuth = :memberAuth WHERE m.userid = :userId")
    void updateMemberAuth(@Param("userId") Integer userId, @Param("memberAuth") MemberAuth memberAuth);
}
