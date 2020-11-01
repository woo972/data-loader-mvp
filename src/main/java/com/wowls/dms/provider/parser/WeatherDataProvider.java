package com.wowls.dms.provider.parser;

import com.wowls.dms.adapter.OpenApiWeatherDataAdapter;
import com.wowls.dms.dto.GridLocationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class WeatherDataProvider {

    private final OpenApiWeatherDataAdapter openApiWeatherDataAdapter;

    public List<String> getWeatherData(List<GridLocationDto> gridLocationDtoList){
        return openApiWeatherDataAdapter.getWeatherData(gridLocationDtoList);
    }
}
