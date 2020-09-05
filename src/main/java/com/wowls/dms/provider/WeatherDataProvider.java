package com.wowls.dms.provider;

import com.wowls.dms.adapter.SkTechXWeatherDataAdapter;
import com.wowls.dms.domain.Weather;
import com.wowls.dms.provider.parser.WeatherDataParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WeatherDataProvider {
    private SkTechXWeatherDataAdapter skTechXWeatherDataAdapter;
    private WeatherDataParser weatherDataParser;

    // TODO: parser를 범용적으로. datasource형식(json,xml,csv 등), target bean을 파라미터로. datasource는 factory로 구현?
    public Weather getCurrentWeatherData(){
//        String currentWeatherData = skTechXWeatherDataAdapter.getCurrentWeatherData();
//        return weatherDataParser.parseToBean(currentWeatherData);
        return null;
    }
}
