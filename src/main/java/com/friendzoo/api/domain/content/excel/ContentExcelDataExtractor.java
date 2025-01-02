package com.friendzoo.api.domain.content.excel;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.util.excel.ExcelDataExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ContentExcelDataExtractor {

    public static List<ContentDTO> extract(MultipartFile file) {
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(file.getBytes()))) {
            ExcelDataExtractor<ContentDTO> extractor = getExtractor();
            return extractor.extract(workbook.getSheetAt(0));
        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일을 읽는 중 오류가 발생했습니다.", e);
        }
    }

    private static ExcelDataExtractor<ContentDTO> getExtractor() {
        return new ExcelDataExtractor<>() {
            @Override
            protected ContentDTO map(Row row) {
                ContentDTO dto = ContentDTO.builder()
                        .divisionId((long) row.getCell(0).getNumericCellValue())
                        .title(row.getCell(1).getStringCellValue().trim())
                        .body(row.getCell(2).getStringCellValue())
                        .tags(getExcelTags(row.getCell(3).getStringCellValue().trim()))
                        .imagePathList(getExcelImageList(row.getCell(4).getStringCellValue().trim()))
                        .build();

                validateValue(dto);
                return dto;
            }
        };
    }

    /**
     * 태그 정보를 ","로 구분하여 List로 반환
     * @param tagInfo 태그 정보 "쿼카,애완동물"
     * @return 태그 List
     */
    private static List<String> getExcelTags(String tagInfo) {
        List<String> tagList = new ArrayList<>();
        // if "," 없을시
        if(tagInfo == null || tagInfo.isEmpty()) {
            return tagList;
        }

        if (!tagInfo.contains(",")) {
            tagList.add(tagInfo);
        } else {
            String[] tags = tagInfo.split(",");
            tagList.addAll(Arrays.asList(tags));
        }

        return tagList;
    }

    /**
     * 이미지 경로 정보를 ","로 구분하여 List로 반환
     * @param imagePathInfo 이미지 경로 정보
     * @return 이미지 경로 List
     */
    private static List<String> getExcelImageList(String imagePathInfo) {
        List<String> imageList = new ArrayList<>();
        // if "," 없을시
        if(imagePathInfo == null || imagePathInfo.isEmpty()) {
            return imageList;
        }
        if (!imagePathInfo.contains(",")) {
            imageList.add(imagePathInfo);
        } else {
            String[] imagePaths = imagePathInfo.split(",");
            imageList.addAll(Arrays.asList(imagePaths));
        }

        return imageList;
    }

    private static void validateValue(ContentDTO dto) {
        if (dto.getTitle().length() > 255) {
            throw new IllegalArgumentException("콘텐츠 명의 길이가 255자를 초과했습니다.");
        }
//        if (dto.getMainImages().length() > 255) {
//            throw new IllegalArgumentException("게시판 메인 image url의 길이가 255자를 초과했습니다.");
//        }
    }

}
