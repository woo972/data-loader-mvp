package com.wowls;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WeatherDataDownloader {
    private static final String targetUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst";
    private static final String serviceKey = "timS1upr5gqf%2BAljM0E5kAz35ti6Ucuj91mGshQ7srSBj8CXi7rGvRCSRvnAQEZTFnGyzlfQMbS5Fg5hiFDtEA%3D%3D";

    public static int TO_GRID = 0;
    public static int TO_GPS = 1;

    public static void main (String[] args) throws IOException {
//    public static void mainMethod() throws IOException {
        // 경일대, 대구 알파시티, 울산 이예로(울산 북부순환도로), 대구 KIAPI, 서울 상암(월드컵경기장), 성남 판교(판교역), 화성 KATRI, 경기 안양(안양시청)
        List<Point> experimentLocationList = new ArrayList<>();
        experimentLocationList.add(convertGRID_GPS(TO_GRID, 35.908583, 128.801170));
        experimentLocationList.add(convertGRID_GPS(TO_GRID, 35.858504, 128.630544));
        experimentLocationList.add(convertGRID_GPS(TO_GRID, 35.581128, 129.357271));
        experimentLocationList.add(convertGRID_GPS(TO_GRID, 35.647633, 128.399901));
        experimentLocationList.add(convertGRID_GPS(TO_GRID, 37.569000, 126.897414));
        experimentLocationList.add(convertGRID_GPS(TO_GRID, 37.395940, 127.111002));
        experimentLocationList.add(convertGRID_GPS(TO_GRID, 37.228474, 126.770331));
        experimentLocationList.add(convertGRID_GPS(TO_GRID, 37.394876, 126.956937));

        String yyyyMMdd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hhmm = LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("hh")) + "00";

        int pointX = 0;
        int pointY = 0;
        for (Point p : experimentLocationList) {
            pointX = (int) p.x;
            pointY = (int) p.y;

            /**
             * 공공데이터포털에서 데이터를 가져온다
             */
            StringBuilder urlBuilder = new StringBuilder(targetUrl); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8")); /*공공데이터포털에서 받은 인증키*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(yyyyMMdd, "UTF-8")); /*15년 12월 1일 발표*/
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(hhmm, "UTF-8")); /*06시 발표(정시단위)*/
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pointX), "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pointY), "UTF-8")); /*예보지점 Y 좌표*/

            System.out.println(">>> url : " + urlBuilder.toString() );

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
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
            System.out.println(sb.toString());

            /**
             * file로 데이터를 내보낸다
             */
            File file = new File("./user-output/openApiCurrentWeather_"+yyyyMMdd+hhmm+".txt");
            try{
                BufferedWriter wr = new BufferedWriter(new FileWriter(file, true));
                wr.write(sb.toString());
                wr.write("\n");
                wr.close();
            }catch (IOException e){
                System.out.println("파일쓰기 에러:"+e.getMessage());
            }

            /**
             * DB에 연결하여 데이터를 입력한다
             */
//            Connection con = null;
//            String driver = "com.mysql.jdbc.Driver";
//            String jdbcUrl = "jdbc:mysql://localhost:3306/mydb";
//            String user = "root";
//            String pw = "root";
//            try {
//                Class.forName(driver);
//                con = DriverManager.getConnection(jdbcUrl, user, pw);
//                System.out.println("[Database 연결 성공]");
//            } catch (SQLException e) {
//                System.out.println("[SQL Error : " + e.getMessage() + "]");
//            } catch (ClassNotFoundException e1) {
//                System.out.println("[JDBC Connector Driver Error : " + e1.getMessage() + "]");
//            } finally {
//                if (con != null) {
//                    try {
//                        con.close();
//                    } catch (Exception e) {
//
//                    }
//                }
//            }
        }
    }
    private static Point convertGRID_GPS(int mode, double lat, double lng) {
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

        if (mode == TO_GRID) {
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
        } else {
            rs.x = lat;
            rs.y = lng;
            double xn = lat - XO;
            double yn = ro - lng + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            } else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                } else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
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