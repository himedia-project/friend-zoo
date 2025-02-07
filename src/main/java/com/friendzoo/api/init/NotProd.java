package com.friendzoo.api.init;

import com.friendzoo.api.domain.member.enums.MemberRole;
import com.friendzoo.api.domain.member.service.MemberService;
import com.friendzoo.api.domain.test.entity.Test;
import com.friendzoo.api.domain.test.repository.TestRepository;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Slf4j
@Configuration
//@Profile({"!prod"})
@RequiredArgsConstructor
public class NotProd {

    private final TestRepository testRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public CommandLineRunner init() {
        return (args) -> {
            log.info("init data...");

            // user - test = 1: N // api 해당 유저 를 조회  (안의 testList 콜렉션 필드를 가져온다.)
            if(testRepository.count() > 0) {
                return;
            }
            if (memberRepository.count() > 0) {
                return;
            }

            Member member = Member.builder()
                    .email("test@test.com")
                    .name("test")
                    .password(passwordEncoder.encode("1234"))
                    .phone("010-1234-5678")
                    .delFlag(false)
                    .build();

            member.addRole(MemberRole.ADMIN);

            Member savedMember = memberRepository.save(member);


            testRepository.saveAll(List.of(
                    Test.builder().title("AAA").member(savedMember).build(),
                    Test.builder().title("BBB").member(savedMember).build(),
                    Test.builder().title("CCC").member(savedMember).build(),
                    Test.builder().title("DDD").member(savedMember).build(),
                    Test.builder().title("EEE").member(savedMember).build()

            ));

//            List<Test> testList = testRepository.findAll();
//            log.info("testList: {}", testList);
//
//            user.addTestList(testList);
        };
    }
}
