package me.dolphago.job;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StackOverFlowConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job myJob() {
        Step extract = extractorStep();
        Step process = filesProcessStep();
        Step cleanup = cleanupStep();

        return jobBuilderFactory.get("myJob")
                                .start(echo("Starting batch job"))

                                .next(extract).on(ExitStatus.FAILED.getExitCode()).to(cleanup)
                                .from(extract).on("*").to(process)

                                .next(process).on(ExitStatus.FAILED.getExitCode()).to(cleanup)
                                .from(process).on("*").to(cleanup)

                                .next(echo("End batch job"))
                                .end()
                                .build();
    }


    @Bean
    public Step extractorStep() {
        return stepBuilderFactory.get("extractorStep")
                                 .tasklet((contribution, chunkContext) -> {
                                     log.info("extractorStep");
                                     return RepeatStatus.FINISHED;
                                 }).build();
    }

    @Bean
    public Step filesProcessStep() {
        return stepBuilderFactory.get("filesProcessStep")
                                 .tasklet((contribution, chunkContext) -> {
                                     log.info("filesProcessStep");
                                     return RepeatStatus.FINISHED;
                                 }).build();
    }

    @Bean
    public Step cleanupStep() {
        return stepBuilderFactory.get("cleanupStep")
                                 .tasklet((contribution, chunkContext) -> {
                                     log.info("cleanupStep");
                                     return RepeatStatus.FINISHED;
                                 }).build();
    }

    public Step echo(String message) {
        return stepBuilderFactory.get("echo-" + message)
                                 .tasklet((contribution, chunkContext) -> {
                                     log.info(message);
                                     return RepeatStatus.FINISHED;
                                 }).build();
    }

}
