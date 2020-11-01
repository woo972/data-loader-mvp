package com.wowls.dms.job;

import com.wowls.dms.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Configuration: Spring Batch의 모든 Job은 @Configuration으로 등록해서 사용
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class WeatherDataJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WeatherService weatherService;

    /**
     * Batch Job 생성 (하나의 배치 단위)
     */
    @Bean
    public Job weatherDataJob() {
        return jobBuilderFactory.get("weatherData")
                .start(saveWeatherData(null))
                .build();
    }

    /**
     * Batch Step 생성
     * tasklet: Step 안에서 수행될 기능 명시 (reader/processor/writer로 쓸 수도 있음)
     */
    @Bean
    @JobScope
    public Step saveWeatherData(@Value("#{jobParameters[requestDate]}") String requestData) {
        return stepBuilderFactory.get("saveWeatherData")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    log.info(">>>>> requestData: {}", requestData);
                    weatherService.save();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}