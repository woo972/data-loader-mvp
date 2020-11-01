package com.wowls.dms.service;

import com.wowls.dms.constant.ExperimentLocation;
import com.wowls.dms.converter.WeatherConverter;
import com.wowls.dms.dto.WeatherRequestDto;
import com.wowls.dms.dto.WeatherResponseDto;
import com.wowls.dms.entity.Weather;
import com.wowls.dms.provider.parser.LocationParser;
import com.wowls.dms.provider.parser.WeatherDataProvider;
import com.wowls.dms.repository.WeatherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherConverter weatherConverter;
    private final WeatherDataProvider weatherDataProvider;
    private final ExperimentLocation experimentLocation;

    @Transactional(readOnly = true)
    public Page<WeatherResponseDto> getWeather(WeatherRequestDto weatherRequestDto, Pageable pageable) {
        Page<Weather> weatherPage = weatherRepository
                .findByDateAndLocationCode(weatherRequestDto.getStartDate(),
                                           weatherRequestDto.getEndDate(),
                                           weatherRequestDto.getLocationCode(),
                                            pageable)
                .orElseThrow(() -> new IllegalArgumentException("no object with these parameter"));
        return new PageImpl<WeatherResponseDto>(weatherPage
                                .getContent()
                                .stream()
                                .map(weatherConverter::convertWeatherToWeatherResponseDto)
                                .collect(Collectors.toList()),
                                pageable,
                                weatherPage.getTotalElements());
    }

    // TODO: bulk로 로딩해야하기 때문에 Spring Data JDBC를 써야함 -> 추후변경
    @Transactional
    public void save(){

        List<String> weatherDataJsonList = weatherDataProvider.getWeatherData(
                experimentLocation.getExperimentTargetLocationList()
                        .stream()
                        .map(LocationParser::convertGpsToGrid)
                        .collect(Collectors.toList()));

        List<Weather> weatherEntityList = weatherDataJsonList
                .stream()
                .map(json -> Weather
                        .builder()
                        .weatherData(json)
                        .build())
                .collect(Collectors.toList());

        weatherRepository.saveAll(weatherEntityList);
    }
}
