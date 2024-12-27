package com.dsg.demo241226.domain.test.service;


import com.dsg.demo241226.domain.test.dto.TestCreateReqDTO;
import com.dsg.demo241226.domain.test.dto.TestResDTO;
import com.dsg.demo241226.domain.test.entity.Test;
import com.dsg.demo241226.domain.test.repository.TestQuerydslRepository;
import com.dsg.demo241226.domain.test.repository.TestRepository;
import com.dsg.demo241226.dto.PageRequestDTO;
import com.dsg.demo241226.dto.PageResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service // component
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final TestQuerydslRepository testQuerydslRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<TestResDTO> getList(Pageable pageable, String keyword) {

        Page<Test> all = testRepository.findAll(pageable);
//        Page<Test> all = testQuerydslRepository.findAll(pageable, keyword);

        return all.map(this::entityToDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponseDTO<TestResDTO> getPage(PageRequestDTO requestDTO) {

        Page<Test> result = testQuerydslRepository.findListBy(requestDTO);

        long totalCount = result.getTotalElements();

        List<TestResDTO> testDTOList = result.stream().map(this::entityToDTO).toList();

        return PageResponseDTO.<TestResDTO>withAll()
                .dtoList(testDTOList)
                .pageRequestDTO(requestDTO)
                .totalCount(totalCount)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public TestResDTO getOne(Long id) {
        return this.entityToDTO(getTest(id));
    }


    @Override
    public Long register(TestCreateReqDTO testDTO) {
        Test savedTest = testRepository.save(this.dtoToEntity(testDTO));
        return savedTest.getId();
    }

    @Override
    public Long modify(Long id, TestCreateReqDTO testDTO) {
        Test test = this.getTest(id);

        test.changeTitle(testDTO.getTitle()); // 수정
        Test mofiedTest = testRepository.save(test);
        return mofiedTest.getId();
    }

    @Override
    public void remove(Long id) {
        Test test = this.getTest(id);
        testRepository.delete(test);
    }


    private Test getTest(Long id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NOT FOUND ID: " + id));
    }
}
