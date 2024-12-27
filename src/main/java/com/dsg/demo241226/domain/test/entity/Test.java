package com.dsg.demo241226.domain.test.entity;

import com.dsg.demo241226.domain.test.enums.*;
import com.dsg.demo241226.domain.user.entity.User;
import com.dsg.demo241226.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;


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

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'N'")
    private TestType type;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'N'")
    private BestType best;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'N'")
    private MdpickType mdpick;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'N'")
    private NewType isnew;

   @Enumerated(EnumType.STRING)
   @ColumnDefault("'N'")
   private SaleType sale;

   @Enumerated(EnumType.STRING)
   @ColumnDefault("'N'")
   private ContentsType content;

   // Dto 형식으로 작성 해볼 것
   // list?best="N"?

    public void changeTitle(String title) {
        this.title = title;
    }
}
