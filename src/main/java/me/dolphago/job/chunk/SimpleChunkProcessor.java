package me.dolphago.job.chunk;

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.api.chunk.ItemWriter;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.listener.MulticasterBatchListener;
import org.springframework.batch.core.step.item.Chunk;
import org.springframework.batch.core.step.item.ChunkProcessor;
import org.springframework.beans.factory.InitializingBean;

public class SimpleChunkProcessor<I, O> implements ChunkProcessor<I>, InitializingBean {

    private ItemProcessor<? super I, ? extends O> itemProcessor;
    private ItemWriter<? super O> itemWriter;
    private final MulticasterBatchListener<I, O> listener = new MulticasterBatchListener<>();

    public SimpleChunkProcessor() {
        this.itemProcessor = null;
        this.itemWriter = null;
    }

    @Override
    public void process(StepContribution contribution, Chunk<I> chunk) throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
