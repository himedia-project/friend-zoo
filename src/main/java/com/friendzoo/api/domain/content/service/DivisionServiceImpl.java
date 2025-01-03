package com.friendzoo.api.domain.content.service;

import com.friendzoo.api.domain.content.entity.Division;
import com.friendzoo.api.domain.content.repository.ContentRepository;
import com.friendzoo.api.domain.content.repository.DivisionRepository;
import com.friendzoo.api.domain.product.dto.CategoryDTO;
import com.friendzoo.api.util.file.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class DivisionServiceImpl implements DivisionService {

    private final DivisionRepository divisionRepository;
    private final CustomFileUtil fileUtil;

    private final ContentRepository contentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDTO> list() {
        return divisionRepository.findAll().stream().map(this::entityToDTO).toList();
    }

    @Override
    public Long register(CategoryDTO dto) {
        // 파일 s3 업로드
        if (dto.getFile() != null || dto.getFile().isEmpty()) {
            MultipartFile file = dto.getFile();
            String uploadFileName = fileUtil.uploadS3File(file);
            dto.setFileName(uploadFileName);
        }
        Division result = divisionRepository.save(this.dtoToEntity(dto));
        return result.getId();
    }

    @Override
    public void remove(Long divisionId) {
        // 연관관계가 있는 카테고리라면 삭제 불가능
        if (contentRepository.existsByDivisionId(divisionId)) {
            throw new IllegalStateException("연관된 콘텐츠가 있어 카테고리 삭제가 불가능합니다.");
        }

        divisionRepository.deleteById(divisionId);
    }
}
