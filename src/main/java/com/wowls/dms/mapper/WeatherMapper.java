package com.wowls.dms.mapper;

import com.wowls.dms.dto.WeatherRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface WeatherMapper {
    String getWeather(WeatherRequestDto weatherRequestDto);
}