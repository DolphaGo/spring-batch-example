package me.dolphago.job.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@StepScope // 이거 없으면 오류 난다!
@Component
public class ScopeTestJobTasklet implements Tasklet {
    @Value("#{jobParameters[requestDate]}")
    private String requestDate;

    public ScopeTestJobTasklet(){
        log.info(">>>>>>>>>>>> tasklet 생성");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info(">>>>>>>> This is Step1");
        log.info(">>>>>>>> requestDate = {}", requestDate);
        return RepeatStatus.FINISHED;
    }
}
