package com.wowls.dms.dto;

import lombok.Builder;
import lombok.Getter;

// 미사용
@Getter
@Builder
public class WeatherUpdateRequestDto {
    private Long id;
    private float temperature;
    private float precipitation;
    private float windDirection;
    private float windSpeed;
    private float atmosphericPressure;
    private float seaLevelPressure;
    private float humidity;
    private float insolation;
    private float sunshineDuration;
}
