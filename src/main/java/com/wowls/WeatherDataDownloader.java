package com.wowls;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * servicekey는 2년마다 갱신필요
 * 다음 갱신 날짜 : 2022.08.23
 */
public class WeatherDataDownloader {
    private static final String currentDataBaseUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst";
    private static final String forecastDataBaseUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst";
    private static final String serviceKey = "timS1upr5gqf%2BAljM0E5kAz35ti6Ucuj91mGshQ7srSBj8CXi7rGvRCSRvnAQEZTFnGyzlfQMbS5Fg5hiFDtEA%3D%3D";

    public static void main (String[] args) {
        // 경일대 1, 울산 이예로 2, 대구 알파시티 3, 대구 KIAPI 4, 서울 상암 5, 경기 판교 6, 화성 KATRI 7, 안양벤처밸리 8
        // 경일대, 울산 이예로(울산 북부순환도로), 대구 알파시티, 대구 KIAPI, 서울 상암(월드컵경기장), 성남 판교(판교역), 화성 KATRI, 경기 안양(평촌 오비즈타워)
        List<Point> experimentLocationList = new ArrayList<>();
        experimentLocationList.add(convertGRID_GPS(35.908583, 128.801170));
        experimentLocationList.add(convertGRID_GPS(35.581128, 129.357271));
        experimentLocationList.add(convertGRID_GPS(35.858504, 128.630544));
        experimentLocationList.add(convertGRID_GPS(35.647633, 128.399901));
        experimentLocationList.add(convertGRID_GPS(37.569000, 126.897414));
        experimentLocationList.add(convertGRID_GPS(37.395940, 127.111002));
        experimentLocationList.add(convertGRID_GPS(37.228474, 126.770331));
        experimentLocationList.add(convertGRID_GPS(37.400062, 126.970250));

        /**
         * 인터페이스 날짜 / 시간 설정
         */

        String yyyyMMdd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hhmm = LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("HH")) + "00";
        String hhmm_fcst = LocalTime.now().format(DateTimeFormatter.ofPattern("HH")) + "00";
        if("2300".equals(hhmm)){
            yyyyMMdd = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }

        for (int idx=0; idx<experimentLocationList.size(); idx++) {
            int pointX = (int) experimentLocationList.get(idx).x;
            int pointY = (int) experimentLocationList.get(idx).y;
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
             * file로 데이터를 내보낸다
             */
//            writeFile(currentData, forecastData, yyyyMMdd, hhmm);

            /**
             * json을 파싱하면 필요한 데이터만 가져온다
             */
            String parsedJson = parseData(currentData, forecastData, yyyyMMdd, hhmm, hhmm_fcst, location);
            System.out.println(">>>> parsedJson:"+parsedJson);
            /**
             * DB에 연결하여 데이터를 입력한다
             */
            writeDb(parsedJson);

        }
    }

    private static void writeDb(String parsedJson) {
        System.out.println("inininin");

        Connection con = null;
        String driver = "org.mariadb.jdbc.Driver";
        String jdbcUrl = "jdbc:mariadb://127.0.0.1:3306/addup";
        String user = "root";
        String pw = "root";
        String sql = "insert into weather (weather_data) values(?)";
        PreparedStatement ps = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(jdbcUrl, user, pw);
            ps = con.prepareStatement(sql);
            ps.setString(1, parsedJson);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[SQL Error : " + e.getMessage() + "]");
        } catch (ClassNotFoundException e1) {
            System.out.println("[JDBC Connector Driver Error : " + e1.getMessage() + "]");
        } finally {
            if (con != null) {
                try {
                    con.close();
                    ps.close();
                } catch (Exception e) {
                    System.out.println("con / ps close Error:"+e.getMessage());
                }
            }
        }
    }

    private static void writeFile(String currentData, String forecastData, String targetDate, String targetTime) {
        File file = new File("./user-output/openApiCurrentWeather_"+targetDate+targetTime+".txt");
        try{
            BufferedWriter wr = new BufferedWriter(new FileWriter(file, true));
            wr.write(currentData);
            wr.write("\n");
            wr.write(forecastData);
            wr.write("\n");
            wr.close();
        }catch (IOException e){
            System.out.println("파일쓰기 에러:"+e.getMessage());
        }
    }

    private static String parseData(String currentData, String forecastData, String targetDate, String targetTime, String fcstTargetTime, String location) {
        // {"response":{"header":{"resultCode":"00","resultMsg":"NORMAL_SERVICE"},"body":{"dataType":"JSON","items":{"item":[{"baseDate":"20200905","baseTime":"0800","category":"PTY","nx":59,"ny":123,"obsrValue":"0"},{"baseDate":"20200905","baseTime":"0800","category":"REH","nx":59,"ny":123,"obsrValue":"66"},{"baseDate":"20200905","baseTime":"0800","category":"RN1","nx":59,"ny":123,"obsrValue":"0"},{"baseDate":"20200905","baseTime":"0800","category":"T1H","nx":59,"ny":123,"obsrValue":"23.1"},{"baseDate":"20200905","baseTime":"0800","category":"UUU","nx":59,"ny":123,"obsrValue":"-2.1"},{"baseDate":"20200905","baseTime":"0800","category":"VEC","nx":59,"ny":123,"obsrValue":"83"},{"baseDate":"20200905","baseTime":"0800","category":"VVV","nx":59,"ny":123,"obsrValue":"-0.2"},{"baseDate":"20200905","baseTime":"0800","category":"WSD","nx":59,"ny":123,"obsrValue":"2.2"}]},"pageNo":1,"numOfRows":10,"totalCount":8}}}
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

    private static Point convertGRID_GPS(double lat, double lng) {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat:위도,  lng:경도), "TO_GPS"(좌표->위경도,  lat:x, lng:y) )
        //

        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        Point rs = new Point();
        rs.lat = lat;
        rs.lng = lng;
        double ra = Math.tan(Math.PI * 0.25 + (lat) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = lng * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
        rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

        return rs;
    }
    static class Point {

        public double lat;
        public double lng;

        public double x;
        public double y;

        public Point(){}
        public Point(double lat, double lng){
            this.lat = lat;
            this.lng = lng;
        }
    }
}