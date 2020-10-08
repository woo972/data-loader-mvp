//package com.wowls.dms.dto;
//
//import com.opencsv.bean.CsvBindByPosition;
//import com.opencsv.bean.CsvDate;
//import lombok.Builder;
//
//import java.util.Date;
//// 미사용
///**
// * locationCode:지점,
// * date:일시,
// * temperature: 기온(°C),
// * precipitation: 누적강수량(mm),
// * windDirection:풍향(deg),
// * windSpeed: 풍속(m/s),
// * atmosphericPressure: 현지기압(hPa),
// * seaLevelPressure: 해면기압(hPa),
// * humidity: 습도(%),
// * insolation: 일사(MJ/m^2),
// * sunshineDuration: 일조(Sec)
// */
//@Builder
//public class WeatherDataSourceDto extends CsvDto{
//    @CsvBindByPosition(position = 0)
//    private int locationCode;
//    @CsvBindByPosition(position = 1)
//    @CsvDate("yyyy-MM-dd") // Csv파일에 저장된 날짜 형식을 Date 타입으로 바꾼다
//    private Date date;
//    @CsvBindByPosition(position = 2)
//    private float temperature;
//    @CsvBindByPosition(position = 3)
//    private float precipitation;
//    @CsvBindByPosition(position = 4)
//    private float windDirection;
//    @CsvBindByPosition(position = 5)
//    private float windSpeed;
//    @CsvBindByPosition(position = 6)
//    private float atmosphericPressure;
//    @CsvBindByPosition(position = 7)
//    private float seaLevelPressure;
//    @CsvBindByPosition(position = 8)
//    private float humidity;
//    @CsvBindByPosition(position = 9)
//    private float insolation;
//    @CsvBindByPosition(position = 10)
//    private float sunshineDuration;
//
//
//}
//
