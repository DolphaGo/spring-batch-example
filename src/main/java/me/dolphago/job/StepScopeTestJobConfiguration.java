package me.dolphago.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepScopeTestJobConfiguration {
    private final ScopeTestJobTasklet tasklet;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean// 아니면 Job의 이름을 Bean(name = " ")로 지정해줄 수 있다. (Bean 이름을 설정하지 않으면 JobName이 메서드 명으로 간다.)
    public Job scopeTestJob() { // Job의 이름은 이 메서드 이름을 가지고 구분한다!
        log.info(">>>>>>>>>>> definition scopeTestJob");
        return jobBuilderFactory.get("ScopeTestJob")
                                .start(scopeTestStep1())
                                .next(scopeTestStep2(null))
                                .build();
    }

    @Bean
//    @JobScope
    public Step scopeTestStep1() {
        log.info(">>>>>>>>>>> definition scopeTestStep1");
        return stepBuilderFactory.get("scopeTestStep1")
                                 .tasklet(tasklet)
                                 .build();
    }

    public Step scopeTestStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
        log.info(">>>>>>>>>>> definition scopeTestStep2");
        return stepBuilderFactory.get("scopeTestStep2")
                                 .tasklet(tasklet)
                                 .build();
    }
}
