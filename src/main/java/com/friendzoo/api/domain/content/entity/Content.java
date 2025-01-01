package com.friendzoo.api.domain.content.entity;

import com.friendzoo.api.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "content")
public class Content extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "division_id", nullable = false)
    private Division division;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "body")
    private String body;

    @ColumnDefault("0")
    @Column(name = "del_flag")
    private Boolean delFlag;

    @ElementCollection
    @Builder.Default
    private List<ContentImage> imageList = new ArrayList<>();


}