//package com.wowls.dms.provider;
//
//import com.wowls.dms.adapter.OpenApiWeatherDataAdapter;
//import com.wowls.dms.constant.ExperimentLocation;
//import com.wowls.dms.entity.Weather;
//import com.wowls.dms.provider.parser.WeatherDataParser;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import java.util.List;
//// 미사용
//@RequiredArgsConstructor
//@Component
//public class WeatherDataProvider {
//    private OpenApiWeatherDataAdapter openApiWeatherDataAdapter;
//    private WeatherDataParser weatherDataParser;
//
//    // TODO: parser를 범용적으로. datasource형식(json,xml,csv 등), target bean을 파라미터로. datasource는 factory로 구현?
//    public List<Weather> getCurrentWeatherData(){
//        ExperimentLocation experimentLocation = new ExperimentLocation();
//        return openApiWeatherDataAdapter.getCurrentWeatherData(experimentLocation.getExperimentTargetLocationList());
//    }
//}
