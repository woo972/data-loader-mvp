package com.wowls.dms.repository;

import com.wowls.dms.entity.Weather;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    @Query(value = "SELECT weather_id, weather_data FROM weather" +
            " WHERE weather_data->'$.date' >= :start_date" +
            " AND weather_data->'$.date' <= :end_date" +
            " AND weather_data->'$.location_code' = :location_code" +
            " ORDER BY weather_data->'$.date'",
            countQuery = "SELECT count(*) FROM weather" +
                    " WHERE weather_data->'$.date' >= :start_date" +
                    " AND weather_data->'$.date' <= :end_date" +
                    " AND weather_data->'$.location_code' = :location_code",
            nativeQuery = true)
    Optional<Page<Weather>> findByDateAndLocationCode(@Param("start_date") String startDate,
                                                      @Param("end_date") String endDate,
                                                      @Param("location_code") String locationCode,
                                                      Pageable paging);
}
