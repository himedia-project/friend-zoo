package com.friendzoo.api.domain.product.excel;

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
@RequestMapping("/api/admin/product/excel")
public class ProductExcelController {

    private final ProductExcelService productExcelService;


    @PostMapping("/register")
    public ResponseEntity<?> registerByExcel(
            @RequestPart(value = "file") MultipartFile batchRegistrationFile,
            @AuthenticationPrincipal MemberDTO userDetailsDto
    ) {
        List<ProductDTO> registrationDtoList = ProductExcelDataExtractor.extract(batchRegistrationFile);
        List<RegistrationFailResponseDTO> response = productExcelService.register(registrationDtoList, userDetailsDto.getEmail());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadExcelFile(
            @RequestBody @Valid ProductIdListDTO requestDto
    ) {
        log.info("AdminBrandExcelController downloadBrandFile run...");

        List<ProductDataDTO> dtoList = productExcelService.getExcelDataList(requestDto);
        ByteArrayResource dataListFile = ExcelGenerator.generateExcelFile(dtoList, "상품 목록");

        return ExcelGenerator.createResponseEntity(dataListFile, "friend_zoo_관리자_상품_목록_" + getNowTimeStr("yyyyMMddHHmmss") + ".xlsx");
    }

}
