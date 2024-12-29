package com.friendzoo.api.domain.test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCreateReqDTO {

    @NotBlank(message = "title은 필수값입니다.")
    @Size(min = 1, max = 10, message = "title은 1자 이상 10자 이하로 입력해주세요.")
    private String title;
}
