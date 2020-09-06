package com.wowls.dms.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GridLocationDto {
    private double x;
    private double y;
}
