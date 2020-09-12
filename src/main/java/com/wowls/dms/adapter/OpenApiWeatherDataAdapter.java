package com.wowls.dms.adapter;

import com.google.common.collect.Lists;
import com.wowls.dms.dto.GpsLocationDto;
import com.wowls.dms.dto.GridLocationDto;
import com.wowls.dms.entity.Weather;
import com.wowls.dms.provider.parser.LocationParser;
import com.wowls.dms.provider.parser.WeatherDataParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class OpenApiWeatherDataAdapter {
//
//    private static final String baseUri = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst";
//    private static final String serviceKey = "timS1upr5gqf%2BAljM0E5kAz35ti6Ucuj91mGshQ7srSBj8CXi7rGvRCSRvnAQEZTFnGyzlfQMbS5Fg5hiFDtEA%3D%3D";
//
    public List<Weather> getCurrentWeatherData(List<GpsLocationDto> experimentTargetLocationList) {
//        String yyyyMMdd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        String hhmm = LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("hh")) + "00";
//
//        StringBuilder urlBuilder = new StringBuilder(baseUri); /*URL*/
//        try{
//            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
//            urlBuilder.append("&" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8")); /*공공데이터포털에서 받은 인증키*/
//            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
//            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
//            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
//            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(yyyyMMdd, "UTF-8")); /*15년 12월 1일 발표*/
//            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(hhmm, "UTF-8")); /*06시 발표(정시단위)*/
//        } catch (UnsupportedEncodingException e){
//            System.out.println("[UnsupportedEncodingEx]"+e.getMessage());
//        }
//
//        List<Weather> weatherList = Lists.newArrayList();
//        WeatherDataParser weatherDataParser = new WeatherDataParser();
//        LocationParser locationParser = new LocationParser();
//        for (GpsLocationDto gpsLocationDto : experimentTargetLocationList) {
//            GridLocationDto gridLocationDto = locationParser.convertGpsToGrid(gpsLocationDto);
//
//            HttpURLConnection conn = null;
//            BufferedReader rd = null;
//            StringBuilder sb = new StringBuilder();
//            try {
//                URL url = new URL(urlBuilder.toString()+"&nx="+gridLocationDto.getX()+"&ny="+gridLocationDto.getY());
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestProperty("Content-type", "application/json");
//                conn.setRequestMethod("GET");
//                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                } else {
//                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//                }
//                String line = "";
//                while ((line = rd.readLine()) != null) {
//                    sb.append(line);
//                }
//                weatherList.add(weatherDataParser.jsonToBean(line));
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }finally {
//                conn.disconnect();
//                try {
//                    rd.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return weatherList;
        return null;
    }
}
