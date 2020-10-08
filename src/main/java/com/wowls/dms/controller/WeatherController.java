package com.wowls.dms.controller;

import com.wowls.dms.dto.WeatherRequestDto;
import com.wowls.dms.dto.WeatherResponseDto;
import com.wowls.dms.entity.Weather;
import com.wowls.dms.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class WeatherController {
    private final WeatherService weatherService;

    @PostMapping("/api/v1/weather")
    public WeatherResponseDto getWeather(@RequestBody WeatherRequestDto weatherRequestDto,
                                         @RequestParam(required = false, value = "pageChunck") int pageChunk,
                                         @RequestParam(required = false, value = "currentPage") int currentPage){
        return weatherService.getWeather(weatherRequestDto, pageChunk, currentPage);
    }

//    @GetMapping("/api/v1/weatherList")
//    public Page<Weather> getWeatherList(){
//        return weatherService.getWeatherList();
//    }
}
