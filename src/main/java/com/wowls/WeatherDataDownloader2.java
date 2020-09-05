package com.wowls;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
// 미사용
public class WeatherDataDownloader2 {
    private static final String baseUri = "http://api.weatherplanet.co.kr/weather/current/hourly?version=%s&lat=%s&lon=%s";

    public static void main (String[] args) throws IOException {
//    public static void mainMethod() throws IOException {
        // 경일대, 대구 알파시티, 울산 이예로(울산 북부순환도로), 대구 KIAPI, 서울 상암(월드컵경기장), 성남 판교(판교역), 화성 KATRI, 경기 안양(안양시청)
        List<Point> experimentLocationList = new ArrayList<>();
        experimentLocationList.add(new Point(35.908583, 128.801170));
        experimentLocationList.add(new Point(35.858504, 128.630544));
        experimentLocationList.add(new Point(35.581128, 129.357271));
        experimentLocationList.add(new Point(35.647633, 128.399901));
        experimentLocationList.add(new Point(37.569000, 126.897414));
        experimentLocationList.add(new Point(37.395940, 127.111002));
        experimentLocationList.add(new Point(37.228474, 126.770331));
        experimentLocationList.add(new Point(37.394876, 126.956937));

        String yyyyMMdd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hhmm = LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("hh")) + "00";

        for (Point p : experimentLocationList) {

            /**
             * 공공데이터포털에서 데이터를 가져온다
             */

            String targetUri = String.format(baseUri,"2.5",String.valueOf(p.lat),String.valueOf(p.lng));
            System.out.println(">>> url : " + targetUri );

            URL url = new URL(targetUri);
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
            File file = new File("./user-output/skTechXCurrentWeather_"+yyyyMMdd+hhmm+".txt");
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

    static class Point {
        public double lat;
        public double lng;

        public Point(){}
        public Point(double lat, double lng){
            this.lat = lat;
            this.lng = lng;
        }
    }
}