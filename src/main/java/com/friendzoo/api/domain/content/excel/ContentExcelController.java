package com.friendzoo.api.domain.content.excel;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.security.MemberDTO;
import com.friendzoo.api.util.excel.ExcelGenerator;
import com.friendzoo.api.util.excel.RegistrationFailResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.friendzoo.api.util.TimeUtil.getNowTimeStr;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/content/excel")
public class ContentExcelController {

    private final ContentExcelService contentExcelService;


    @PostMapping("/register")
    public ResponseEntity<?> registerByExcel(
            @RequestPart(value = "file") MultipartFile batchRegistrationFile,
            @AuthenticationPrincipal MemberDTO userDetailsDto
    ) {
        List<ContentDTO> registrationDtoList = ContentExcelDataExtractor.extract(batchRegistrationFile);
        List<RegistrationFailResponseDTO> response = contentExcelService.register(registrationDtoList, userDetailsDto.getEmail());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadExcelFile(
            @RequestBody @Valid ContentIdListDTO requestDto
    ) {
        log.info("AdminBrandExcelController downloadBrandFile run...");

        List<ContentDataDTO> dtoList = contentExcelService.getExcelDataList(requestDto);
        ByteArrayResource dataListFile = ExcelGenerator.generateExcelFile(dtoList, "콘텐츠 목록");

        return ExcelGenerator.createResponseEntity(dataListFile, "friend_zoo_관리자_콘텐츠_목록_" + getNowTimeStr("yyyyMMddHHmmss") + ".xlsx");
    }

}
