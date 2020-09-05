package com.wowls.dms.provider;

import net.e175.klaus.solarpositioning.*;
import java.util.GregorianCalendar;

// 미사용
public class SolarPositionDataProvider {
    public void test() {
        final GregorianCalendar dateTime = new GregorianCalendar();

        AzimuthZenithAngle position = SPA.calculateSolarPosition(
                dateTime,
                48.21, // latitude (degrees)
                16.37, // longitude (degrees)
                190, // elevation (m)
                DeltaT.estimate(dateTime), // delta T (s)
                1010, // avg. air pressure (hPa)
                11); // avg. air temperature (°C)
        System.out.println();
        System.out.println("SPA: " + position);

        GregorianCalendar[] res = SPA.calculateSunriseTransitSet(
                dateTime,
                70.978056, // latitude
                25.974722, // longitude
                68); // delta T
        for(GregorianCalendar r: res){
            System.out.println(r);
        }

    }

}