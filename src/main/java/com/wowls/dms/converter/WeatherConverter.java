package com.wowls.dms.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowls.dms.dto.WeatherResponseDto;
import com.wowls.dms.entity.Weather;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WeatherConverter{
    public WeatherResponseDto convertWeatherJsonToPojo(String weatherJson){
        if(StringUtils.isNotEmpty(weatherJson)){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(weatherJson, WeatherResponseDto.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new WeatherResponseDto();
    }

    public WeatherResponseDto convertWeatherToPojo(Weather weather) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            WeatherResponseDto weatherResponseDto
                    = objectMapper.readValue(weather.getWeatherData(), WeatherResponseDto.class);
            weatherResponseDto.setWeatherId(weather.getWeatherId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new WeatherResponseDto();
    }
}
