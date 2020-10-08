package com.wowls.dms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
public class WeatherResponseDto {
    @JsonProperty("weather_id")
    private Long weatherId;
    private String version;
    private String date;
    @JsonProperty("location_code")
    private String locationCode;
    private String condition;
    private String temperature;
    private String humidity;
    @JsonProperty("precipitation_type")
    private String precipitationType;
    private String precipitation;
    @JsonProperty("wind_direction")
    private String windDirection;
    @JsonProperty("wind_speed")
    private String windSpeed;
    @JsonProperty("wind_component_ew")
    private String windComponentEw;
    @JsonProperty("wind_component_sn")
    private String windComponentSn;
    private String lightning;
}
