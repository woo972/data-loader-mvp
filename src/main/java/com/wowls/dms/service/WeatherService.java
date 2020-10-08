package com.wowls.dms.service;

import com.wowls.dms.converter.WeatherConverter;
import com.wowls.dms.dto.WeatherRequestDto;
import com.wowls.dms.dto.WeatherResponseDto;
import com.wowls.dms.entity.Weather;
import com.wowls.dms.dto.WeatherUpdateRequestDto;
//import com.wowls.dms.provider.WeatherDataProvider;
import com.wowls.dms.mapper.WeatherMapper;
import com.wowls.dms.repository.WeatherJdbc;
import com.wowls.dms.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WeatherService {

//    private final WeatherDataProvider weatherDataProvider;
    private final WeatherRepository weatherRepository;
    private final WeatherConverter weatherConverter;
//    private final WeatherMapper weatherMapper;
//    private final WeatherJdbc weatherJdbc;

    public WeatherResponseDto getWeather(WeatherRequestDto weatherRequestDto,
                                         int pageChunk, int currentPage) {

        Weather weather = weatherRepository
                .findByDateAndLocationCode(weatherRequestDto)
                .orElseThrow(() -> new IllegalArgumentException("no object with these parameter"));
        return weatherConverter.convertWeatherToPojo(weather);
    }

//    public Page<Weather> getWeatherList() {
//        Page<Weather> d = weatherRepository.findAll(PageRequest.of(1,20));
//        return d;
//    }

//    @Transactional
//    public WeatherResponseDto getWeather(WeatherRequestDto weatherRequestDto) {
//        System.out.println(weatherRequestDto.toString());
//        String weatherJson =  weatherRepository
//                .findByDateAndLocationCode(
//                        weatherRequestDto.getStartDateTime(),
//                        String.valueOf(weatherRequestDto.getLocationCode()))
//                .orElseThrow(() -> new IllegalArgumentException("no object with these parameter"));
//        return weatherConverter.convertWeatherJsonToPojo(weatherJson);
//    }

    @Transactional
    public void update(List<WeatherUpdateRequestDto> weatherUpdateRequestDtoList) {
        // 하나씩 불러와서 처리하면 connection을 계속 맺고 끊고...?
//        List<Weather> weatherList = Collections.emptyList();
//        for(WeatherUpdateRequestDto weatherUpdateRequestDto : weatherUpdateRequestDtoList){
//            Weather weather = weatherRepository.findById(weatherUpdateRequestDto.getId())
//                    .orElseThrow(() -> new IllegalArgumentException("no object with that id exist"));
//        }
    }

    // TODO: bulk로 로딩해야하기 때문에 Spring Data JDBC를 써야함
//    @Transactional
//    public void save() {
//        List<Weather> weatherList = weatherDataProvider.getCurrentWeatherData();
//        weatherRepository.saveAll(weatherList);
//    }

}
