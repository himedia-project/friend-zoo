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

    // 태그이름 리스트 -> ex. postma 등록시 라이온,행스터 "," 를 구분으로 넣어주면 된다.
    private List<String> tags;

    private String title;
    private String body;

    // 파일 입력값
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    // 파일 업로드한 url 응답값
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

    // excel 등록시 image path list
    private List<String> imagePathList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;
}
