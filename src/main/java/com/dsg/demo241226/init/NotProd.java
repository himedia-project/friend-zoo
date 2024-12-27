package com.dsg.demo241226.init;

import com.dsg.demo241226.domain.test.entity.Test;
import com.dsg.demo241226.domain.test.repository.TestRepository;
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
    public CommandLineRunner init() {
        return (args) -> {
            log.info("init data...");

            if(testRepository.count() > 0) {
                return;
            }

            testRepository.saveAll(List.of(
                    Test.builder().title("AAA").build(),
                    Test.builder().title("BBB").build(),
                    Test.builder().title("CCC").build(),
                    Test.builder().title("DDD").build(),
                    Test.builder().title("EEE").build()

            ));
        };
    }
}
