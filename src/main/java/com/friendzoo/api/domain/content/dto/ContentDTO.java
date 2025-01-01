package com.friendzoo.api.domain.content.dto;

import com.friendzoo.api.domain.product.entity.Heart;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    private Long categoryId;
    private String category;

    private String title;
    private String body;

    private Heart heart;

    // 파일 입력값
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    // 파일 업로드한 url 응답값
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

}
