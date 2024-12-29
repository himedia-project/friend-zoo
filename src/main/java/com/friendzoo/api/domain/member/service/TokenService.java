package com.friendzoo.api.domain.member.service;

import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.member.entity.Token;
import com.friendzoo.api.domain.member.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    /**
     * RefreshToken 저장
     * @param accessToken accessToken
     * @param refreshToken refreshToken
     * @param member member
     */
    public void saveRefreshToken(String accessToken, String refreshToken, Member member) {

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .member(member)
                .build();
        tokenRepository.save(token);

    }
}
