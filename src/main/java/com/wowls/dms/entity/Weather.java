package com.wowls.dms.entity;

import lombok.Builder;
import lombok.Getter;
import javax.persistence.*;

@Builder
@Getter
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_id")
    private Long weatherId;
    @Column(name = "weather_data")
    private String weatherData;
}
