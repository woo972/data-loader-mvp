package com.wowls.dms.service;

import com.wowls.dms.converter.WeatherConverter;
import com.wowls.dms.dto.WeatherRequestDto;
import com.wowls.dms.dto.WeatherResponseDto;
import com.wowls.dms.entity.Weather;
import com.wowls.dms.dto.WeatherUpdateRequestDto;
import com.wowls.dms.repository.WeatherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherConverter weatherConverter;

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
