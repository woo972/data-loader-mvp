package com.wowls.dms.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GpsLocationDto {
    private double latitude;
    private double longitude;
}
