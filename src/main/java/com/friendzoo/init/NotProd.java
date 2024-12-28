package com.friendzoo.init;

import com.friendzoo.domain.test.entity.Test;
import com.friendzoo.domain.test.repository.TestRepository;
import com.friendzoo.domain.user.entity.User;
import com.friendzoo.domain.user.repository.UserRepository;
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
    public CommandLineRunner init(UserRepository userRepository) {
        return (args) -> {
            log.info("init data...");

            // user - test = 1: N // api 해당 유저 를 조회  (안의 testList 콜렉션 필드를 가져온다.)
//            if(testRepository.count() > 0) {
//                return;
//            }

            User user = userRepository.save(User.builder()
                    .email("test@test.com")
                    .name("테스트")
                    .password("1234")
                    .address("test test test")
                    .role("USER")
                    .delFlag(false)
                    .build());


            testRepository.saveAll(List.of(
                    Test.builder().title("AAA").user(user).build(),
                    Test.builder().title("BBB").user(user).build(),
                    Test.builder().title("CCC").user(user).build(),
                    Test.builder().title("DDD").user(user).build(),
                    Test.builder().title("EEE").user(user).build()

            ));

//            List<Test> testList = testRepository.findAll();
//            log.info("testList: {}", testList);
//
//            user.addTestList(testList);
        };
    }
}
