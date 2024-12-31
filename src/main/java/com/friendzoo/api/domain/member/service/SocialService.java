package com.friendzoo.api.domain.member.service;

import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.member.enums.MemberRole;
import com.friendzoo.api.domain.member.repository.MemberRepository;
import com.friendzoo.api.props.SocialProps;
import com.friendzoo.api.security.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SocialService {

    private final RestTemplate restTemplate;

    // @Value 어노테이션을 사용하면 final 키워드를 사용할 수 없다.
    // 생성자 주입방식 말고 필드 주입방식으로 객체주입

    private final SocialProps socialProps;
    private final MemberRepository memberRepository;

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;


    public String getKakaoAccessToken(String code) {
        log.info("getKakaoAccessToken start...");

        // 문자열로 반환할려면?
        String kakaoTokenURL = socialProps.getKakao().getTokenUri();

        String clientID = socialProps.getKakao().getClientId();
        String clientSecret = socialProps.getKakao().getClientSecret();
        String redirectURI = socialProps.getKakao().getRedirectUri();


        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoTokenURL)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientID)
                .queryParam("client_secret", clientSecret)
                .queryParam("redirect_uri", redirectURI)
                .queryParam("code", code)
                .build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(uriBuilder.toString(), HttpMethod.POST, entity, LinkedHashMap.class);

        log.info("response: {}", response);

        LinkedHashMap<String, String> bodyMap = response.getBody();

        log.info("bodyMap: {}", bodyMap);

        return bodyMap.get("access_token");
    }


    @Transactional
    public MemberDTO getKakaoMember(String accessToken) {
        log.info("getKakaoMember start...");
        String email = this.getEmailFromKakaoAccessToken(accessToken);
        log.info("getKakaoMember email: {}", email);

        Optional<Member> result = memberRepository.findById(email);

        // 기존의 회원
        if (result.isPresent()) {
            MemberDTO memberDTO = memberService.entityToDTO(result.get());
            return memberDTO;
        }

        // 회원이 아니었다면 닉네임은 '소셜 회원'으로 패스워드는 임의로 생성
        Member socialMember = this.makeSocialMember(email);
        memberRepository.save(socialMember);
        return memberService.entityToDTO(socialMember);
    }


    public String getGoogleAccessToken(String code) {
        log.info("getGoogleAccessToken start...");

        String googleTokenURL = socialProps.getGoogle().getTokenUri();

        String clientID = socialProps.getGoogle().getClientId();

        String clientSecret = socialProps.getGoogle().getClientSecret();

        String redirectURI = socialProps.getGoogle().getRedirectUri();


        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(googleTokenURL)
                .queryParam("code", code)
                .queryParam("client_id", clientID)
                .queryParam("client_secret", clientSecret)
                .queryParam("redirect_uri", redirectURI)
                .queryParam("grant_type", "authorization_code")
                .build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(uriBuilder.toString(), HttpMethod.POST, entity, LinkedHashMap.class);

        log.info("response: {}", response);

        LinkedHashMap<String, String> bodyMap = response.getBody();

        log.info("bodyMap: {}", bodyMap);
        return bodyMap.get("access_token");
    }


    @Transactional
    public MemberDTO getGoogleMember(String accessToken) {
        log.info("getGoogleMember start...");
        String email = this.getEmailFromGoogleAccessToken(accessToken);
        log.info("getGoogleMember email: {}", email);

        Optional<Member> result = memberRepository.findById(email);

        // 기존의 회원
        if (result.isPresent()) {
            MemberDTO memberDTO = memberService.entityToDTO(result.get());
            return memberDTO;
        }

        // 회원이 아니었다면 닉네임은 '소셜 회원'으로 패스워드는 임의로 생성
        Member socialMember = this.makeSocialMember(email);
        memberRepository.save(socialMember);
        return memberService.entityToDTO(socialMember);
    }


    public String getNaverAccessToken(String code, String state) {
        log.info("getNaverAccessToken start...");

        // 네이버는
        String naverTokenURL = socialProps.getNaver().getTokenUri();
        String clientID = socialProps.getNaver().getClientId();
        String clientSecret = socialProps.getNaver().getClientSecret();


        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(naverTokenURL)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientID)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .queryParam("state", state)
                .build();

        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uriBuilder.toString(), HttpMethod.POST, entity, LinkedHashMap.class);

        log.info("response: {}", response);

        LinkedHashMap<String, String> bodyMap = response.getBody();

        log.info("bodyMap: {}", bodyMap);
        return bodyMap.get("access_token");
    }

    @Transactional
    public MemberDTO getNaverMember(String accessToken) {
        log.info("getNaverMember start...");
        // 네이버는 이메일을 바로 제공해주기 때문에 바로 사용
        String email = this.getEmailFromNaverAccessToken(accessToken);
        log.info("getNaverMember email: {}", email);

        Optional<Member> result = memberRepository.findById(email);

        // 기존의 회원
        if (result.isPresent()) {
            MemberDTO memberDTO = memberService.entityToDTO(result.get());
            return memberDTO;
        }

        // 회원이 아니었다면 닉네임은 '소셜 회원'으로 패스워드는 임의로 생성
        Member socialMember = this.makeSocialMember(email);
        memberRepository.save(socialMember);
        return memberService.entityToDTO(socialMember);

    }


    private String getEmailFromKakaoAccessToken(String accessToken) {

        log.info("getEmailFromKakaoAccessToken start...");
        String kakaoGetUserURL = socialProps.getKakao().getUserInfoUri();

        if (accessToken == null) {
            throw new RuntimeException("Access Token is null");
        }


        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response
                = restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, LinkedHashMap.class);

        log.info("response: {}", response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
        log.info("bodyMap: {}", bodyMap);

        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");
        log.info("kakaoAccount: {}", kakaoAccount);

        return kakaoAccount.get("email");
    }

    private String getEmailFromGoogleAccessToken(String accessToken) {
        log.info("getEmailFromGoogleAccessToken start...");
        // 리소스 uri
        String googleGetUserURL = socialProps.getGoogle().getUserInfoUri();

        if (accessToken == null) {
            throw new RuntimeException("Access Token is null");
        }


        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(googleGetUserURL).build();

        ResponseEntity<LinkedHashMap> response
                = restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, LinkedHashMap.class);

        log.info("response: {}", response);

        LinkedHashMap<String, String> bodyMap = response.getBody();

        log.info("bodyMap: {}", bodyMap);

        return bodyMap.get("email");
    }


    private String getEmailFromNaverAccessToken(String accessToken) {
        log.info("getEmailFromNaverAccessToken start...");

        String naverGetUserURL = socialProps.getNaver().getUserInfoUri();

        if (accessToken == null) {
            throw new RuntimeException("Access Token is null");
        }


        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(naverGetUserURL).build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, LinkedHashMap.class);

        log.info("response: {}", response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        log.info("bodyMap: {}", bodyMap);


        return bodyMap.get("response").get("email").toString();
    }


    public Member makeSocialMember(String email) {
        String tempPassword = memberService.makeTempPassword();

        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(tempPassword))
                .name("소셜회원")
                .build();
        member.addRole(MemberRole.USER);
        return member;
    }
}
