package com.wowls.dms.constant;

import com.google.common.collect.Lists;
import com.wowls.dms.dto.GpsLocationDto;
import java.util.List;

public class ExperimentLocation {
    private static final List<GpsLocationDto> locationList = Lists.newArrayList(
            GpsLocationDto.builder().latitude(35.908583).longitude(128.801170).build(),
            GpsLocationDto.builder().latitude(35.858504).longitude(128.630544).build(),
            GpsLocationDto.builder().latitude(35.581128).longitude(129.357271).build(),
            GpsLocationDto.builder().latitude(35.647633).longitude(128.399901).build(),
            GpsLocationDto.builder().latitude(37.569000).longitude(126.897414).build(),
            GpsLocationDto.builder().latitude(37.395940).longitude(127.111002).build(),
            GpsLocationDto.builder().latitude(37.228474).longitude(126.770331).build(),
            GpsLocationDto.builder().latitude(37.394876).longitude(126.956937).build()
    );
    public List<GpsLocationDto> getExperimentTargetLocationList(){
        return locationList;
    }
}
