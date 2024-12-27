package com.dsg.demo241226.domain.test.entity;

import com.dsg.demo241226.domain.user.entity.User;
import com.dsg.demo241226.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@Entity
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Table(name = "test")
public class Test extends BaseEntity {
 ///  user : test = 1 : N
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY) // jpa
    @JoinColumn(name = "email")  // fk
    private User user;

    public void changeTitle(String title) {
        this.title = title;
    }
}
