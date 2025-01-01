package com.friendzoo.api.domain.product.service;

import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Category;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.enums.ProductBest;
import com.friendzoo.api.domain.product.enums.ProductMdPick;
import com.friendzoo.api.domain.product.repository.CategoryRepository;
import com.friendzoo.api.domain.product.repository.ProductRepository;
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
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final CustomFileUtil fileUtil;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Override
    public PageResponseDTO<ProductDTO> list(PageRequestDTO requestDTO) {

        log.info("ProductAdminService list...");

        Page<Product> result = productRepository.findListBy(requestDTO);

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(result.stream().map(this::entityToDTO).collect(Collectors.toList()))
                .totalCount(result.getTotalElements())
                .pageRequestDTO(requestDTO)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDTO getOne(Long id) {
        Product product = this.getEntity(id);
        return this.entityToDTO(product);
    }

    @Override
    public Long register(ProductDTO dto) {

        // 파일 업로드 처리
        if(dto.getFiles() != null || !dto.getFiles().isEmpty()) {
            List<MultipartFile> files = dto.getFiles();
            List<String> uploadFileNames = fileUtil.uploadS3File(files);
            log.info("uploadFileNames: {}", uploadFileNames);
            dto.setUploadFileNames(uploadFileNames);
        }

        // 카테고리
        Category category = this.getCategory(dto.getCategoryId());

        // 실제 저장 처리
        Product result = productRepository.save(this.dtoToEntity(dto, category));

        return result.getId();
    }

    @Override
    public Long modify(Long id, ProductDTO dto) {

        Product product = this.getEntity(id);

        ProductDTO oldDTO = this.entityToDTO(product);

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

        // 상품 수정
        product.changeCategory(this.getCategory(dto.getCategoryId()));
        product.changeName(dto.getName());
        product.changePrice(dto.getPrice());
        product.changeDiscountPrice(dto.getDiscountPrice() == null ? 0 : dto.getDiscountPrice());
        product.changeDescription(dto.getDescription());
        product.changeStockNumber(dto.getStockNumber());
        product.changeBest(dto.getBest() == null ? ProductBest.N : dto.getBest());
        product.changeMdPick(dto.getMdPick() == null ? ProductMdPick.N : dto.getMdPick());

        product.clearImageList();

        // 새로 업로드할 파일들을 새로 추가
        List<String> uploadFileNames = dto.getUploadFileNames();

        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(product::addImageString);
        }

        return product.getId();

    }


    @Override
    public void remove(Long id) {
        Product product = this.getEntity(id);
        productRepository.modifyDeleteFlag(product.getId());
    }


    /**
     * Entity 찾기
     * @param id 엔티티 id
     * @return DTO
     */
    private Product getEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 엔티티가 존재하지 않습니다. id: " + id));
    }

    /**
     * 상품 카테고리 찾기
     * @param categoryId 카테고리 id
     * @return 카테고리
     */
    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다. id: " + categoryId));
    }
}
