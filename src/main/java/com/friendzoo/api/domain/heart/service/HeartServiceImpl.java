package com.friendzoo.api.domain.heart.service;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.content.entity.ContentImage;
import com.friendzoo.api.domain.content.repository.ContentRepository;
import com.friendzoo.api.domain.content.service.ContentService;
import com.friendzoo.api.domain.heart.entity.Heart;
import com.friendzoo.api.domain.heart.repository.HeartRepository;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.member.service.MemberService;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService {
    private final HeartRepository heartRepository;

    private final ProductService productService;
    private final ContentRepository contentRepository;

    private final MemberService memberService;
    private final ContentService contentService;

//    @Override
//    public List<HeartDTO> getHeartItemCheck(String email, Long id) {
//        List<Heart> dtoLists = heartRepository.findrelatedItem(email,id);
//        List<HeartDTO> result = new ArrayList<>();
//        if(!dtoLists.isEmpty()) {
//            if (!dtoLists.isEmpty()) {
//                heartRepository.deleteById(dtoLists.get(0).getId());
//                result  = dtoLists.stream()
//                        .map(this::entityToDTO) // Product를 ProductDTO로 변환
//                        .collect(Collectors.toList()); // 리스트로 수집
//                return result;
//            }
//
//        }
//        else {
//            Long product_id = id;
//            String email_address = email;
//
//            Heart heart = new Heart();
//            heart.setProduct(product_id);
//            heart.setEmail(email_address);
//            heart.setContentId(null);
//
//            Heart heart_entity = heartRepository.save(heart);
//            result = List.of(entityToDTO(heart_entity));
//            return result;
//        }
//
//        return result;
//    }

    @Override
    public void heartProduct(Long productId, String email) {
        // productId & email
        Product product = this.productService.getEntity(productId);
        Member member = this.memberService.getMember(email);


        Optional<Heart> heartProduct = heartRepository.findHeartProduct(email, productId);
        if (heartProduct.isPresent()) {
            // heart o -> heart 삭제
            heartRepository.delete(heartProduct.get());
        } else {
            //   heart x -> heart 생성
            Heart heart = Heart.builder().member(member)
                    .product(product)
                    .content(null)
                    .build();
            heartRepository.save(heart);
        }

    }

    @Override
    public void heartContent(Long contentId, String email) {

        Content content = this.contentService.getEntity(contentId);
        Member member = this.memberService.getMember(email);
        Optional<Heart> heartContent = heartRepository.findHeartContent(email, contentId);
        if (heartContent.isPresent()) {
            // heart o -> heart 삭제
            heartRepository.delete(heartContent.get());
        } else {
            //   heart x -> heart 생성
            Heart heart = Heart.builder().member(member)
                    .product(null)
                    .content(content)
                    .build();
            heartRepository.save(heart);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> findProductListByMember(String email) {
        return heartRepository.findProductListByMember(email).stream()
                .map(productService::entityToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ContentDTO> findContentListByMember(String email) {
        List<Content> result = contentRepository.findHeartList(email);
        List<ContentDTO> dtoResult =  result.stream().map(content -> {
            // isHeart 여부 <- content, email
            boolean isHeart = heartRepository.findExistedHeartContent(email,content.getId());
            if(isHeart == false){
                return null;
            }
//            List<Content> getTags = contentRepository.findDetailTagList(content_id);
            ContentDTO dto = ContentDTO.builder()
                    .id(content.getId())
                    .divisionId(content.getDivision().getId())
                    .divisionName(content.getDivision().getName())
                    .title(content.getTitle())
                    .uploadFileNames(content.getImageList().stream().map(ContentImage::getImageName).toList())
                    .heartCount((long) content.getHeartList().size())
                    .isHeart(isHeart)
                    .createdAt(content.getCreatedAt())
                    .modifiedAt(content.getModifiedAt())
                    .tags(content.getContentTagList().stream().map(contentTag -> contentTag.getTag().getName()).toList())
//                    .tags()
                    .build();

            return dto;
        }).toList();
        return dtoResult;
    }




}
