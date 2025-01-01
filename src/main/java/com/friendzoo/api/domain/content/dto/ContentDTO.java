package com.friendzoo.api.domain.content.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.friendzoo.api.domain.product.entity.Heart;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ContentDTO {

    private Long id;

    private Long divisionId;
    private String divisionName;


    private String title;
    private String body;

    // 파일 입력값
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    // 파일 업로드한 url 응답값
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;
}
