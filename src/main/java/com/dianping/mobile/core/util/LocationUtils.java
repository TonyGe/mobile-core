package com.dianping.mobile.core.util;


public final class LocationUtils {

    private LocationUtils() {
    }

    private static double parseToRadian(double input) {
        return input / 180 * Math.PI;
    }

    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        double dlat = parseToRadian(lat2) - parseToRadian(lat1);
        double dlon = parseToRadian(lng2) - parseToRadian(lng1);
        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) + Math.cos(lat1)
                * Math.cos(lat2) * Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return 6371000 * c;
    }

}
