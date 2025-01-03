package com.friendzoo.api.domain.content.service;



import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.dto.PageRequestDTO;
import com.friendzoo.api.dto.PageResponseDTO;

import java.util.List;

public interface ContentService {
    List<ContentDTO> getContent(ContentDTO contentDTO);

    List<ContentDTO> findDetailListBy(String email,Long content_id);

    List<ContentDTO> findListBy(String email);

//    List<ContentDTO> findDetailListBy(String email);


}
