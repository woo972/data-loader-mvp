package com.wowls.dms.controller;

import com.wowls.dms.dto.WeatherRequestDto;
import com.wowls.dms.dto.WeatherResponseDto;
import com.wowls.dms.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class WeatherController {
    private final WeatherService weatherService;

    @PostMapping("/api/v1/weather")
    public Page<WeatherResponseDto> getWeather(@RequestBody WeatherRequestDto weatherRequestDto,
                                               @PageableDefault (page = 0, size = 5)
//                                               @SortDefault.SortDefaults({
//                                                       @SortDefault(sort = "date", direction = Sort.Direction.ASC),
//                                                       @SortDefault(sort = "location", direction = Sort.Direction.ASC)
//                                               })
                                                       Pageable pageable){
        return weatherService.getWeather(weatherRequestDto, pageable);
    }

    @GetMapping("/api/v1/open-weather")
    public void saveWeather(){
        weatherService.save();
    }
}
