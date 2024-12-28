package com.friendzoo.domain.user.service;

import com.friendzoo.domain.test.dto.TestResDTO;
import com.friendzoo.domain.user.dto.UserDto;
import com.friendzoo.domain.user.entity.User;

public interface UserService {
    UserDto findUser(String email);

    // user -> dto
    default UserDto entityToDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .tests(user.getTestList().stream().map(test ->
                        TestResDTO.builder()
                                .id(test.getId())
                                .title(test.getTitle())
                                .createdAt(test.getCreatedAt())
                                .modifiedAt(test.getModifiedAt())
                                .build()
                ).toList())
                .build();
    }
}
