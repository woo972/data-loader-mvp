package com.wowls.dms.domain;

import com.wowls.dms.dto.WeatherUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int locationCode;
    @Column(nullable = false)
    private Date date;
    private float temperature;
    private float precipitation;
    private float windDirection;
    private float windSpeed;
    private float atmosphericPressure;
    private float seaLevelPressure;
    private float humidity;
    private float insolation;
    private float sunshineDuration;

    @Builder
    public Weather(int locationCode, Date date, float temperature, float precipitation,
                   float windDirection, float windSpeed, float atmosphericPressure,
                   float seaLevelPressure, float humidity, float insolation, float sunshineDuration){
        this.locationCode = locationCode;
    }

    public void update(WeatherUpdateRequestDto weatherUPdateRueqestDto){
        this.temperature = weatherUPdateRueqestDto.getTemperature();
        this.precipitation = weatherUPdateRueqestDto.getPrecipitation();
    }
}
