package com.wowls.dms.service;

import com.wowls.dms.entity.Weather;
import com.wowls.dms.dto.WeatherUpdateRequestDto;
import com.wowls.dms.provider.WeatherDataProvider;
import com.wowls.dms.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

/**
 * data 밀어 넣기 / 날씨 테이블 생성
 * 배치로 필요한 정보만 추출하는 작은 테이블 생성
 * 1단계 : cron -> jdbc -> table
 * 2단계 : qua
 */

@RequiredArgsConstructor
@Service
public class WeatherService {

    private WeatherDataProvider weatherDataProvider;
    private WeatherRepository weatherRepository;

    @Transactional
    public void update(List<WeatherUpdateRequestDto> weatherUpdateRequestDtoList){
        // 하나씩 불러와서 처리하면 connection을 계속 맺고 끊고...?
//        List<Weather> weatherList = Collections.emptyList();
//        for(WeatherUpdateRequestDto weatherUpdateRequestDto : weatherUpdateRequestDtoList){
//            Weather weather = weatherRepository.findById(weatherUpdateRequestDto.getId())
//                    .orElseThrow(() -> new IllegalArgumentException("no object with that id exist"));
//        }
    }

    // TODO: bulk로 로딩해야하기 때문에 Spring Data JDBC를 써야함
    @Transactional
    public void save(){
        List<Weather> weatherList = weatherDataProvider.getCurrentWeatherData();
        weatherRepository.saveAll(weatherList);
    }
}
