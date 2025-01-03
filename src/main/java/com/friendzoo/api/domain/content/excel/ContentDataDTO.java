package com.friendzoo.api.domain.content.excel;

import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.content.entity.ContentImage;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.entity.ProductImage;
import com.friendzoo.api.util.excel.ExcelColumn;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class ContentDataDTO {

    @ExcelColumn(header = "콘텐츠 번호")
    private Long id;

    @ExcelColumn(header = "제목")
    private String title;

    @ExcelColumn(header = "내용")
    private String body;

    @ExcelColumn(header = "이미지 경로 리스트")
    private String imagePathList;

    public static ContentDataDTO from(Content content) {
        return ContentDataDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .body(content.getBody())
                .imagePathList(getMergeStr(content.getImageList().stream().map(ContentImage::getImageName).toList()))
                .build();
    }

    private static String getMergeStr(@NotNull List<String> list) {
        return String.join(",", list);
    }
}
