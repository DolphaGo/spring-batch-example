package me.dolphago.controller;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class JobLauncherController { // 웹서버에서 배치 관리하는 것은 권장하지 않음, 예제를 위한 코드일 뿐
    private final JobLauncher jobLauncher;
    private final List<Job> jobs;

    @GetMapping("/launchjob")
    public String handle(@RequestParam("fileName") String fileName, @RequestParam("jobName") String jobName) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("input.file.name", fileName)
                .addLong("requestDate", System.currentTimeMillis())
                .toJobParameters();

        log.info("요청한 JobName = {} ",jobName);

        jobs.stream()
            .filter(j -> j.getName().equals(jobName))
            .findFirst()
            .map(j -> {
                try {
                    return jobLauncher.run(j, jobParameters);
                } catch (Exception e) {
                    log.info(e.getMessage());
                    throw new IllegalStateException("Run Job 중 Exception 발생");
                }
            });
        return "DONE";
    }
}
