package com.wowls.dms.dto;

import lombok.*;
import lombok.experimental.Accessors;

import javax.annotation.PostConstruct;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@RequiredArgsConstructor
public class WeatherRequestDto {
    private Long id;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String locationCode;
    private String startDateTime;
    private String endDateTime;

    public String getStartDateTime(){
        return this.startDate + this.startTime;
    }
}

