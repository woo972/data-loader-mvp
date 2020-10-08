//package com.wowls.dms.job;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.JobScope;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @Configuration: Spring Batch의 모든 Job은 @Configuration으로 등록해서 사용
// */
//@Slf4j
//@RequiredArgsConstructor
//@Configuration
//public class SimpleJobConfiguration {
//    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
//    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음
//
//    /**
//     * Batch Job 생성 (하나의 배치 단위)
//     */
//    @Bean
//    public Job simpleJob() {
//        return jobBuilderFactory.get("simpleJob")
//                .start(simpleStep1(null))
//                .next(simpleStep2(null))
//                .build();
//    }
//
//    /**
//     * Batch Step 생성
//     * tasklet: Step 안에서 수행될 기능 명시 (reader/processor/writer로 쓸 수도 있음)
//     */
//    @Bean
//    @JobScope
//    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestData) {
//        return stepBuilderFactory.get("simpleStep1")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info(">>>>> This is Step1");
//                    log.info(">>>>> requestData: {}", requestData);
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//
//    @Bean
//    @JobScope
//    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestData) {
//        return stepBuilderFactory.get("simpleStep2")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info(">>>>> This is Step2");
//                    log.info(">>>>> requestData: {}", requestData);
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//}