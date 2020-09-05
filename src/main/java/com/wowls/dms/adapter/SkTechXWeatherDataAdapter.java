package com.wowls.dms.adapter;

import org.springframework.stereotype.Component;
// 미사용
@Component
public class SkTechXWeatherDataAdapter {

    private final static String baseUri = "http://api.weatherplanet.co.kr/weather/current/hourly?version=%s&lat=%s&lon=%s";
    private final static String apiVesrsion = "2.5";

    public String getCurrentWeatherData(double lat, double lon){
        String targetUri = String.format(baseUri,apiVesrsion,String.valueOf(lat),String.valueOf(lon));
        System.out.println("target:"+targetUri);

        return targetUri;
    }
}
