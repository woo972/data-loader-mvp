package com.wowls.dms.constant;

import com.google.common.collect.Lists;
import com.wowls.dms.dto.GpsLocationDto;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ExperimentLocation {
    // 경일대 1, 울산 이예로 2, 대구 알파시티 3, 대구 KIAPI 4, 서울 상암 5, 경기 판교 6, 화성 KATRI 7, 안양벤처밸리 8
    // 경일대, 울산 이예로(울산 북부순환도로), 대구 알파시티, 대구 KIAPI, 서울 상암(월드컵경기장), 성남 판교(판교역), 화성 KATRI, 경기 안양(평촌 오비즈타워)
    private static final List<GpsLocationDto> locationList = Lists.newArrayList(
            GpsLocationDto.builder().latitude(35.908583).longitude(128.801170).build(),
            GpsLocationDto.builder().latitude(35.581128).longitude(129.357271).build(),
            GpsLocationDto.builder().latitude(35.858504).longitude(128.630544).build(),
            GpsLocationDto.builder().latitude(35.647633).longitude(128.399901).build(),
            GpsLocationDto.builder().latitude(37.569000).longitude(126.897414).build(),
            GpsLocationDto.builder().latitude(37.395940).longitude(127.111002).build(),
            GpsLocationDto.builder().latitude(37.228474).longitude(126.770331).build(),
            GpsLocationDto.builder().latitude(37.400062).longitude(126.970250).build()
    );
    public List<GpsLocationDto> getExperimentTargetLocationList(){
        return locationList;
    }
}