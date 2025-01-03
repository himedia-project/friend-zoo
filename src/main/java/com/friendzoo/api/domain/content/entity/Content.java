package com.friendzoo.api.domain.content.entity;

import com.friendzoo.api.domain.heart.entity.Heart;
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


    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    private List<ContentTag> contentTagList = new ArrayList<>();

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    private List<Heart> heartList = new ArrayList<>();

    /**
     * 콘텐츠 이미지, 콘텐츠에 추가
     *
     * @param image 이미지
     */
    public void addImage(ContentImage image) {

        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    /**
     * 콘텐츠 이미지 파일, 콘텐츠이미지에 추가
     *
     * @param fileName 이미지 파일명
     */
    public void addImageString(String fileName) {

        ContentImage contentImage = ContentImage.builder()
                .imageName(fileName)
                .build();
        this.addImage(contentImage);
    }

    /**
     * 콘텐츠 이미지 리스트 초기화
     */
    public void clearImageList() {
        this.imageList.clear();
    }

    public void changeDivision(Division division) {
        this.division = division;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeBody(String body) {
        this.body = body;
    }
}