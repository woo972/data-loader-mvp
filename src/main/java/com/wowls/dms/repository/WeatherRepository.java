package com.wowls.dms.repository;

import com.wowls.dms.dto.WeatherRequestDto;
import com.wowls.dms.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    @Query(value = "SELECT * FROM weather" +
            " WHERE weather_data->\"$.date\" = :#{weatherRequest.startDataTime}" +
            " and weather_data->\"$.location_code\" = :#{weatherRequest.locationCode}", nativeQuery = true)
    Optional<Weather> findByDateAndLocationCode(@Param("weatherRequest") WeatherRequestDto weatherRequestDto);
}
