package com.wowls.dms.adapter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wowls.dms.dto.GridLocationDto;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class OpenApiWeatherDataAdapter {

    private static final String currentDataBaseUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst";
    private static final String forecastDataBaseUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst";
    private static final String serviceKey = "timS1upr5gqf%2BAljM0E5kAz35ti6Ucuj91mGshQ7srSBj8CXi7rGvRCSRvnAQEZTFnGyzlfQMbS5Fg5hiFDtEA%3D%3D";


    public List<String> getWeatherData(List<GridLocationDto> experimentTargetLocationList) {

        /**
         * 인터페이스 날짜 / 시간 설정
         */

        String yyyyMMdd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hhmm = LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("HH")) + "00";
        String hhmm_fcst = LocalTime.now().format(DateTimeFormatter.ofPattern("HH")) + "00";
        if("2300".equals(hhmm)){
            yyyyMMdd = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        List<String> weatherJsonDataList = Lists.newArrayList();

        for (int idx=0; idx<experimentTargetLocationList.size(); idx++) {
            int pointX = (int) experimentTargetLocationList.get(idx).getNx();
            int pointY = (int) experimentTargetLocationList.get(idx).getNy();
            String location = String.valueOf(idx + 1);

            /**
             * 공공데이터포털에서 실황 데이터를 가져온다
             */
            String currentDataUrl = getUrl(currentDataBaseUrl, yyyyMMdd, hhmm, pointX, pointY);
            String currentData = getData(currentDataUrl);

            /**
             * 공공데이터포털에서 예보 데이터를 가져온다
             */
            String forecastDataUrl = getUrl(forecastDataBaseUrl, yyyyMMdd, hhmm, pointX, pointY);
            String forecastData = getData(forecastDataUrl);

            /**
             * json을 파싱하면 필요한 데이터만 가져온다
             */
            weatherJsonDataList.add(parseData(currentData, forecastData, yyyyMMdd, hhmm, hhmm_fcst, location));
        }
        return weatherJsonDataList;
    }

    String parseData(String currentData, String forecastData, String targetDate, String targetTime, String fcstTargetTime, String location) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> resultMap = new LinkedHashMap<>();

        /**
         * 추후 중복된 부분 리팩토링
         */
        try {
            resultMap.put("version", "1");
            resultMap.put("date", targetDate+targetTime);
            resultMap.put("location_code", location);

            Map<String, Object> rawMap = objectMapper.readValue(currentData, new TypeReference<Map<String, Object>>(){});
            Map<String, Object> responseMap = (Map<String, Object>) rawMap.get("response");
            Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
            Map<String, Object> itemsMap = (Map<String, Object>) bodyMap.get("items");
            List<Map<String,Object>> itemList = (List<Map<String, Object>>) itemsMap.get("item");
            for(Map<String, Object> item : itemList){
                String CATEGORY = String.valueOf(item.get("category"));
                String OBSR_VALUE = String.valueOf(item.get("obsrValue"));
                String KEY = CATEGORY;
                String VALUE = OBSR_VALUE;

                switch (CATEGORY){
                    case "PTY":
                        KEY = "precipitation_type";
                        switch (OBSR_VALUE){
                            case "1": VALUE = "비"; break;
                            case "2": VALUE = "비,눈"; break;
                            case "3": VALUE = "눈"; break;
                            case "4": VALUE = "소나기"; break;
                            case "5": VALUE = "빗방울"; break;
                            case "6": VALUE = "빗방울,눈날림"; break;
                            case "7": VALUE = "눈날림"; break;
                            case "8": VALUE = "눈"; break;
                            case "0":
                            default: VALUE = "없음"; break;
                        }
                        break;
                    case "T1H": KEY = "temperature"; break;
                    case "RN1": KEY = "precipitation"; break;
                    case "REH": KEY = "humidity"; break;
                    case "VEC": KEY = "wind_direction"; break;
                    case "WSD": KEY = "wind_speed"; break;
                    case "UUU": KEY = "wind_component_ew"; break;
                    case "VVV": KEY = "wind_component_sn"; break;
                }
                if(KEY != null) resultMap.put(KEY, VALUE);
            }

            rawMap = objectMapper.readValue(forecastData, new TypeReference<Map<String, Object>>(){});
            responseMap = (Map<String, Object>) rawMap.get("response");
            bodyMap = (Map<String, Object>) responseMap.get("body");
            itemsMap = (Map<String, Object>) bodyMap.get("items");
            itemList = (List<Map<String, Object>>) itemsMap.get("item");
            for(Map<String, Object> item : itemList){
                /**
                 * 실황 정보에 존재하지 않는 값만 예보에서 가져온다
                 */
                if(fcstTargetTime.equals(String.valueOf(item.get("fcstTime")))){
                    String CATEGORY = String.valueOf(item.get("category"));
                    String FCST_VALUE = String.valueOf(item.get("fcstValue"));

                    String KEY = null;
                    String VALUE = null;
                    switch (CATEGORY){
                        case "SKY":
                            KEY = "condition";
                            switch (FCST_VALUE){
                                case "3": VALUE = "구름많음"; break;
                                case "4": VALUE = "흐림"; break;
                                case "1":
                                default: VALUE = "맑음"; break;
                            }
                            break;
                        case "LGT":
                            KEY = "lightning";
                            switch (FCST_VALUE){
                                case "1": VALUE = "확률낮음"; break;
                                case "2": VALUE = "확률보통"; break;
                                case "3": VALUE = "확률높음"; break;
                                case "0":
                                default: VALUE = "확률없음"; break;
                            }
                            break;
                    }
                    if(KEY != null) resultMap.put(KEY, VALUE);
                }
            }
            return objectMapper.writer().writeValueAsString(resultMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getData(String targetUrl) {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    private static String getUrl(String baseUrl, String yyyyMMdd, String hhmm, int pointX, int pointY) {
        try {
            StringBuilder urlBuilder = new StringBuilder(baseUrl); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(yyyyMMdd, "UTF-8")); /*15년 12월 1일 발표*/
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(hhmm, "UTF-8")); /*06시 발표(정시단위)*/
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pointX), "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pointY), "UTF-8")); /*예보지점 Y 좌표값*/
            return urlBuilder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
