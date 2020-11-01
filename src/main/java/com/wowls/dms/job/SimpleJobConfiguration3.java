package com.wowls.dms.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration3 {
    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음

    @Bean
    public Job simpleJob3() {
        return jobBuilderFactory.get("simpleJob3")
                .start(conditionalStep1())
                    .on("FAILED")   // ExitStatus가 Failed이면
                    .to(conditionalStep3()) // ConditionalStep3로 이동
                    .on("*")    // step3 결과에 상관 없이
                    .end()  // step3로 이동하면 flow 종료
                .from(conditionalStep1())
                    .on("*")    // ExistStatus가 어떤 경우이든
                    .to(conditionalStep2()) // ConditionalStep2로 이동
                    .next(conditionalStep3())   // step2 정상종료시 ConditionalStep3로 이동
                    .on("*")    // step3 결과에 상관없이
                    .end()  // step3로 이동하면 flow 종료
                .end()  // job 종료
                .build();
    }

    /**
     * @JobScope 없음 주의
     */
    @Bean
    public Step conditionalStep1() {
        return stepBuilderFactory.get("conditionalStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is conditionalStep1");
//                    contribution.setExitStatus(ExitStatus.FAILED); // ExitStatus 지정
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalStep2() {
        return stepBuilderFactory.get("conditionalStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is conditionalStep2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalStep3() {
        return stepBuilderFactory.get("conditionalStep3")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is conditionalStep3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}