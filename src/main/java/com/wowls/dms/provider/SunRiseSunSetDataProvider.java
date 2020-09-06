package com.wowls.dms.provider;

import com.wowls.dms.entity.SunRiseSunSet;

import java.util.Calendar;

public class SunRiseSunSetDataProvider {
    public SunRiseSunSet getSunRiseSunSet() {
        Calendar[] sunriseSunset = ca.rmen.sunrisesunset.SunriseSunset.getSunriseSunset(Calendar.getInstance(), 35.858504, 128.630544);
        System.out.println("Sunrise at: " + sunriseSunset[0].getTime());
        System.out.println("Sunset at: " + sunriseSunset[1].getTime());
        return SunRiseSunSet.builder().sunRise(sunriseSunset[0].getTime()).sunSet(sunriseSunset[1].getTime()).build();
    }
}
