package com.friendzoo.api.domain.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentImage {

    @Size(max = 255)
    @Column(name = "image_name")
    private String imageName;

    @NotNull
    @Column(name = "ord", nullable = false)
    private Integer ord;

    public void setOrd(int ord) {
        this.ord = ord;
    }
}