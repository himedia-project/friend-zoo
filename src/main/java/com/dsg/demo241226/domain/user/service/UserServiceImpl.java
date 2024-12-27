package com.dsg.demo241226.domain.user.service;

import com.dsg.demo241226.domain.user.dto.UserDto;
import com.dsg.demo241226.domain.user.entity.User;
import com.dsg.demo241226.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDto findUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        return this.entityToDto(user);
    }
}
