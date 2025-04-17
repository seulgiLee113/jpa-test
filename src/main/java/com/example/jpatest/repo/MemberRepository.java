package com.example.jpatest.repo;

import com.example.jpatest.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByUserId(String userId);

    @Query(value = "select * from tbl_member where id=:n", nativeQuery = true)
    MemberEntity findByContent(@Param("n") long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tbl_member SET user_id = :userId, user_name = :name, password = :pwd, role = :role WHERE id = :id", nativeQuery = true)
    int updateContent(@Param("userId") String userId,
                      @Param("name") String userName,
                      @Param("pwd") String password,
                      @Param("role") String role,
                      @Param("id") Long id);
}
