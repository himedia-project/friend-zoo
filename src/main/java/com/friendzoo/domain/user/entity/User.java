package com.friendzoo.domain.user.entity;

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
@Table(name = "user_tbl")
public class User extends BaseEntity {

    @Id
    private String email;
    private String name;

    private String password;

    private String phone;

    private String address;

    private String role;

    private boolean delFlag;

    public void changeDel(boolean delFlag) {
        this.delFlag = delFlag;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default // fetchjoin
    private List<Test> testList = new ArrayList<>();

}
