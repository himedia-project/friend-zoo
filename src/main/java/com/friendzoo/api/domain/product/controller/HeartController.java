package com.friendzoo.api.domain.product.controller;

import com.friendzoo.api.domain.product.dto.HeartDTO;
import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.service.HeartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/heart")
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

    @PostMapping("/detail/{email}/{id}")
    public ResponseEntity<List<HeartDTO>> heartItemCheck(@PathVariable String email
            , @PathVariable Long id) {
        List<HeartDTO> dtoLists = heartService.getHeartItemCheck(email,id);
        return null;
    }
}
