package me.dolphago.job.tasklet;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.item.Chunk;
import org.springframework.batch.core.step.item.ChunkProcessor;
import org.springframework.batch.core.step.item.ChunkProvider;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ChunkOrientedTasklet<I> implements Tasklet {

    private static final String INPUT_KEY = "DolphaGo";
    private final ChunkProcessor<I> chunkProcessor;
    private final ChunkProvider<I> chunkProvider;
    private boolean buffering = true;
    private static Log logger = LogFactory.getLog(ChunkOrientedTasklet.class);

    public ChunkOrientedTasklet(ChunkProvider<I> chunkProvider, ChunkProcessor<I> chunkProcessor) {
        this.chunkProvider = chunkProvider;
        this.chunkProcessor = chunkProcessor;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Chunk<I> inputs = (Chunk<I>) chunkContext.getAttribute(INPUT_KEY);
        if (inputs == null) {
            inputs = chunkProvider.provide(contribution); // Reader에서 데이터를 가져옵니다.
            if (buffering) {
                chunkContext.setAttribute(INPUT_KEY, inputs);
            }
        }

        chunkProcessor.process(contribution, inputs); // Processor & Writer 처리
        chunkProvider.postProcess(contribution, inputs);

        if (inputs.isBusy()) {
            logger.debug("Inputs still busy");
            return RepeatStatus.CONTINUABLE;
        }

        chunkContext.removeAttribute(INPUT_KEY);
        chunkContext.setComplete();

        if (logger.isDebugEnabled()) {
            logger.debug("Inputs not busy, ended: " + inputs.isEnd());
        }
        return RepeatStatus.continueIf(!inputs.isEnd());

    }
}
