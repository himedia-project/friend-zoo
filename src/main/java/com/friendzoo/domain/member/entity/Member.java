package com.friendzoo.domain.member.entity;

import com.friendzoo.domain.test.entity.Test;
import com.friendzoo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@SuperBuilder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;

    private String password;

    private String phone;

    private String role;

    private boolean delFlag;

    public void changeDel(boolean delFlag) {
        this.delFlag = delFlag;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default // fetchjoin
    private List<Test> testList = new ArrayList<>();

}
