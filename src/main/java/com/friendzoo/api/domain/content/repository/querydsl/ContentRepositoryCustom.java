package com.friendzoo.api.domain.content.repository.querydsl;

import com.friendzoo.api.domain.content.dto.ContentDTO;
import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.dto.PageRequestDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContentRepositoryCustom {

    Page<Content> findListBy(PageRequestDTO requestDTO);

    List<Content> findListByEmail(String email);

    List<Content> findDetailListBy(String email, Long content_id);

    List<Content> findDetailTagList(Long content_id);

    List<Content> findByIdList(List<Long> idList);
}
