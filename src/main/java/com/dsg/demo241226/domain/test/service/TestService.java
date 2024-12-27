package com.dsg.demo241226.domain.test.service;


import com.dsg.demo241226.domain.test.dto.TestCreateReqDTO;
import com.dsg.demo241226.dto.PageRequestDTO;
import com.dsg.demo241226.dto.PageResponseDTO;
import com.dsg.demo241226.domain.test.dto.TestResDTO;
import com.dsg.demo241226.domain.test.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TestService {

    Page<TestResDTO> getList(Pageable pageable, String keyword);

    PageResponseDTO<TestResDTO> getPage(PageRequestDTO requestDTO);

    TestResDTO getOne(Long id);

    Long register(TestCreateReqDTO testDTO);

    Long modify(Long id, TestCreateReqDTO testDTO);

    default TestResDTO entityToDTO(Test test) {
        return TestResDTO.builder()
                .id(test.getId())
                .title(test.getTitle())
                .createdAt(test.getCreatedAt())
                .modifiedAt(test.getModifiedAt())
                .build();
    }

    default Test dtoToEntity(TestCreateReqDTO testDTO) {
        return Test.builder()
                .title(testDTO.getTitle())
                .build();
    }

    void remove(Long id);

}
