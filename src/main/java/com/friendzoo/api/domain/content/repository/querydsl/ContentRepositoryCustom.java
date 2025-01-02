package com.friendzoo.api.domain.content.repository.querydsl;

import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContentRepositoryCustom {

    Page<Content> findListBy(PageRequestDTO requestDTO);

    List<Content> findByIdList(List<Long> idList);
}
