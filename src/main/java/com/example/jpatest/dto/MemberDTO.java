package com.example.jpatest.dto;

import com.example.jpatest.domain.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {

    private Long id;
    private String userId;
    private String userName;
    private String password;
    private String role;

    public MemberDTO(MemberEntity entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.userName = entity.getUserName();
        this.password = entity.getPassword();
        this.role = entity.getRole();
    }
}
