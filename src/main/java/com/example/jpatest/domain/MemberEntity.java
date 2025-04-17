package com.example.jpatest.domain;

import com.example.jpatest.dto.MemberDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name="tbl_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", unique = true, length = 20)
    private String userId;

    @NotNull
    @Column(name="user_name", nullable = false, length = 30)
    private String userName;

    @NotNull
    @Column(name = "password", nullable = false,length = 30)
    private String password;

    @Column(nullable = false)
    private String role;

    @PrePersist
    public void prePersist() {
        if(this.role == null)
            this.role = "USER";
    }

    public MemberEntity(MemberDTO memberDTO) {
        this.userId = memberDTO.getUserId();
        this.userName = memberDTO.getUserName();
        this.password = memberDTO.getPassword();
        this.role = memberDTO.getRole();
    }
}
