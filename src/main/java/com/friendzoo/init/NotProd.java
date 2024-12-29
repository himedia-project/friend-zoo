package com.friendzoo.init;

import com.friendzoo.domain.test.entity.Test;
import com.friendzoo.domain.test.repository.TestRepository;
import com.friendzoo.domain.member.entity.Member;
import com.friendzoo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Slf4j
@Configuration
@Profile({"test"})
@RequiredArgsConstructor
public class NotProd {

    private final TestRepository testRepository;


    @Bean
    public CommandLineRunner init(MemberRepository memberRepository) {
        return (args) -> {
            log.info("init data...");

            // user - test = 1: N // api 해당 유저 를 조회  (안의 testList 콜렉션 필드를 가져온다.)
//            if(testRepository.count() > 0) {
//                return;
//            }

            Member member = memberRepository.save(Member.builder()
                    .email("test@test.com")
                    .name("테스트")
                    .password("1234")
                    .role("USER")
                    .delFlag(false)
                    .build());


            testRepository.saveAll(List.of(
                    Test.builder().title("AAA").member(member).build(),
                    Test.builder().title("BBB").member(member).build(),
                    Test.builder().title("CCC").member(member).build(),
                    Test.builder().title("DDD").member(member).build(),
                    Test.builder().title("EEE").member(member).build()

            ));

//            List<Test> testList = testRepository.findAll();
//            log.info("testList: {}", testList);
//
//            user.addTestList(testList);
        };
    }
}
