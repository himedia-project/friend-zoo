package com.friendzoo.api.domain.content.excel;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.repository.ContentRepository;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.repository.ProductRepository;
import com.friendzoo.api.util.excel.RegistrationFailResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentExcelService {

    private final ContentRepository contentRepository;
    private final ContentCreator contentCreator;


    public List<RegistrationFailResponseDTO> register(List<ContentDTO> dtoList, String email) {
        List<RegistrationFailResponseDTO> failRowList = new ArrayList<>();

        for (int i = 0; i < dtoList.size(); i++) {
            try {
                contentCreator.create(dtoList.get(i));
            } catch (IllegalArgumentException e) {
                // 1행 부터 시작이기 때문에 2를 더한다.
                failRowList.add(new RegistrationFailResponseDTO(i + 2, e.getMessage()));
            } catch (Exception e) {
                failRowList.add(new RegistrationFailResponseDTO(i + 2, "Fail" + e.getMessage()));
            }
        }

        return failRowList;
    }

    public List<ContentDataDTO> getExcelDataList(ContentIdListDTO requestDto) {
        return contentRepository.findByIdList(requestDto.getIdList()).stream()
                .map(ContentDataDTO::from)
                .toList();
    }
}
