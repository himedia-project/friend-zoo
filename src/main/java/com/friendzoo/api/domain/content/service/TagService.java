package com.friendzoo.api.domain.content.service;

import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.domain.content.entity.ContentTag;
import com.friendzoo.api.domain.content.entity.Tag;
import com.friendzoo.api.domain.content.repository.ContentTagRepository;
import com.friendzoo.api.domain.content.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ContentTagRepository contentTagRepository;

    public void registerTags(List<String> tags) {
        log.info("registerTags............");
        tags.forEach(this::registerTag);
    }

    /**
     * 태그를 등록한다.
     * @param tag 태그 이름
     */
    public void registerTag(String tag) {
        log.info("registerTag............");
        // 이미 존재하는 태그라면 등록하지 않는다.
        if(tagRepository.existsByName(tag)) {
            return;
        }

        tagRepository.save(Tag.builder().name(tag).build());

    }

    /**
     * 컨텐츠와 태그를 연결해서 contentTag에 저장
     * @param content 콘텐츠
     * @param tags 태그이름 리스트
     */
    public void registerContentTag(Content content, List<String> tags) {
        //
        tags.forEach(tag -> {
            // 태그가 존재하지 않는다면 등록
            this.registerTag(tag);
            // 태그와 컨텐츠를 연결
            contentTagRepository.save(ContentTag.of(content, this.getEntity(tag)));
        });
    }

    /**
     *  태그 이름으로 태그 엔티티를 가져온다.
     * @param tagName 태그 이름
     * @return 태그 엔티티
     */
    private Tag getEntity(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found: " + tagName));
    }
}
