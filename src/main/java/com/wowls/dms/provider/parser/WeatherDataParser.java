package com.wowls.dms.provider.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowls.dms.entity.Weather;
import org.springframework.stereotype.Component;

import java.io.IOException;
// 미사용
// TODO: parser 범용으로 따로 빼기
@Component
public class WeatherDataParser {
    public Weather jsonToBean(String currentWeatherData) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(currentWeatherData, Weather.class);
        } catch (IOException e){
            System.out.println("[IOEx]"+e.getMessage());
        }
        return null;
    }
}
