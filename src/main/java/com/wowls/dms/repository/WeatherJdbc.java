package com.wowls.dms.repository;

import com.wowls.dms.dto.WeatherRequestDto;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class WeatherJdbc {

    public String getWeather(WeatherRequestDto weatherRequestDto){
        Connection con = null;
        String driver = "com.mysql.cj.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/addup?useSSL=true&verifyServerCertificate=false";
        String user = "d2x";
        String pw = "elxndprtm0-";
        // 5.7.9 이상에서만 작동
        String sql = "SELECT weather_id, weather_data FROM weather"
                + " WHERE weather_data->\"$.date\" = '"+weatherRequestDto.getStartDateTime()+"'"
                + " and weather_data->\"$.location_code\" = '"+weatherRequestDto.getLocationCode()+"'";
//        String sql = "SELECT weather_id, weather_data FROM weather"
//                + " WHERE JSON_EXTRACT(weather_data,\"$.date\") = '"+weatherRequestDto.getStartDateTime()+"'"
//                + " and JSON_EXTRACT(weather_data,\"$.location_code\") = '"+weatherRequestDto.getLocationCode()+"'";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result="";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(jdbcUrl, user, pw);
            ps = con.prepareStatement(sql);
//            ps.setString(1, weatherRequestDto.getStartDateTime());
//            ps.setString(1, weatherRequestDto.getLocationCode());
            rs = ps.executeQuery();
            while(rs.next()){
                result = rs.getString("weather_data");
            }
        } catch (
        SQLException e) {
            System.out.println("[SQL Error : " + e.getMessage() + "]");
        } catch (ClassNotFoundException e1) {
            System.out.println("[JDBC Connector Driver Error : " + e1.getMessage() + "]");
        } finally {
            if (con != null) {
                try {
                    rs.close();
                    con.close();
                    ps.close();
                } catch (Exception e) {
                    System.out.println("con / ps close Error:"+e.getMessage());
                }
            }
        }
        return result;
    }
}
