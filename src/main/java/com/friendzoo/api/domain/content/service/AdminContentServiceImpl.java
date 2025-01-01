package com.friendzoo.api.domain.content.service;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.content.entity.Division;
import com.friendzoo.api.domain.content.repository.ContentRepository;
import com.friendzoo.api.domain.content.repository.DivisionRepository;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;
import com.friendzoo.api.util.file.CustomFileUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AdminContentServiceImpl implements AdminContentService{

    private final CustomFileUtil fileUtil;

    private final TagService tagService;

    private final ContentRepository contentRepository;
    private final DivisionRepository divisionRepository;

    @Transactional(readOnly = true)
    @Override
    public PageResponseDTO<ContentDTO> getList(PageRequestDTO requestDTO) {
        log.info("getList..............");

        Page<Content> result = contentRepository.findListBy(requestDTO);
        return PageResponseDTO.<ContentDTO>withAll()
                .dtoList(result.stream().map(this::entityToDTO).toList())
                .totalCount(result.getTotalElements())
                .pageRequestDTO(requestDTO)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ContentDTO getOne(Long id) {
        return this.entityToDTO(getEntity(id));
    }

    @Override
    public Long register(ContentDTO dto) {

        // 파일 업로드 처리
        if(dto.getFiles() != null || !dto.getFiles().isEmpty()) {
            List<MultipartFile> files = dto.getFiles();
            List<String> uploadFileNames = fileUtil.uploadS3File(files);
            log.info("uploadFileNames: {}", uploadFileNames);
            dto.setUploadFileNames(uploadFileNames);
        }

        // 카테고리
        Division division = this.getDivision(dto.getDivisionId());

        // 실제 저장 처리
        Content result = contentRepository.save(this.dtoToEntity(dto, division));

        // 태그 등록
        if(dto.getTags() != null && !dto.getTags().isEmpty()) {
            List<String> tags = dto.getTags();
            tagService.registerTags(tags);
            // 태그 등록 후 content_tag 테이블에 저장
            tagService.registerContentTag(result, tags);
        }

        return result.getId();
    }

    @Override
    public Long modify(Long id, ContentDTO dto) {

        Content content = this.getEntity(id);

        ContentDTO oldDTO = this.entityToDTO(content);

        // 파일 업로드 처리
        //기존의 파일들 (데이터베이스에 존재하는 파일들 - 수정 과정에서 삭제되었을 수 있음)
        List<String> oldFileNames = oldDTO.getUploadFileNames();

        //새로 업로드 해야 하는 파일들
        List<MultipartFile> files = dto.getFiles();

        //새로 업로드되어서 만들어진 파일 이름들
        List<String> currentUploadFileNames = fileUtil.uploadS3File(files);

        //화면에서 변화 없이 계속 유지된 파일들
        List<String> uploadedFileNames = dto.getUploadFileNames();

        //유지되는 파일들  + 새로 업로드된 파일 이름들이 저장해야 하는 파일 목록이 됨
        if (currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {

            uploadedFileNames.addAll(currentUploadFileNames);

        }

        //기존 파일들 중에서 화면에서 삭제된 파일들을 제거
        if (oldFileNames != null && !oldFileNames.isEmpty()) {

            //지워야 하는 파일 목록 찾기
            //예전 파일들 중에서 지워져야 하는 파일이름들
            List<String> removeFiles = oldFileNames.stream()
                    .filter(fileName -> !uploadedFileNames.contains(fileName)).toList();

            //실제 파일 삭제
            fileUtil.deleteS3Files(removeFiles);
        }

        // 콘텐츠 수정
        content.changeDivision(this.getDivision(dto.getDivisionId()));
        content.changeTitle(dto.getTitle());
        content.changeBody(dto.getBody());

        content.clearImageList();

        // 새로 업로드할 파일들을 새로 추가
        List<String> uploadFileNames = dto.getUploadFileNames();

        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(content::addImageString);
        }

        // 태그 삭제후 새로 등록
        if(dto.getTags() != null && !dto.getTags().isEmpty()) {
            List<String> tags = dto.getTags();
            tagService.removeContentTag(content);
            tagService.registerTags(tags);
            // 태그 등록 후 content_tag 테이블에 저장
            tagService.registerContentTag(content, tags);
        }


        return content.getId();
    }


    @Override
    public void remove(Long id) {
        Content content = getEntity(id);
        contentRepository.modifyDeleteFlag(content.getId());
    }




    /**
     * Entity 찾기
     * @param id Entity
     * @return DTO
     */
    private Content getEntity(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물이 없습니다. id=" + id));
    }

    /**
     * 콘텐츠 카테고리 찾기
     * @param divisionId 카테고리 id
     * @return 카테고리
     */
    private Division getDivision(Long divisionId) {
        return divisionRepository.findById(divisionId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다. id: " + divisionId));
    }
}
