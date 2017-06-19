/**
 * 
 */
package com.dianping.mobile.core.base.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * @author kewen.yao
 *
 */
public class Point {
	
	public static final int MAP_GPS = 0;
	public static final int MAP_GOOGLE = 1;
	public static final int MAP_MAPBAR = 2;
	public static final int MAP_BAIDU = 3;
	public static final int MAP_AMAP = 4;
	
	private int mapType = MAP_GPS;
	private double lat;
	private double lng;
	
	public Point(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
		this.mapType = MAP_GPS;
	}
	
	public Point(double lat, double lng, int mapType) {
		this.lat = lat;
		this.lng = lng;
		this.mapType = mapType;
	}

	public int getMapType() {
		return mapType;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}
	
	private static final Set<Integer> SET_MAPTYPE = new HashSet<Integer>();
	static {
		SET_MAPTYPE.add(MAP_GPS);
		SET_MAPTYPE.add(MAP_GOOGLE);
		SET_MAPTYPE.add(MAP_MAPBAR);
		SET_MAPTYPE.add(MAP_BAIDU);
		SET_MAPTYPE.add(MAP_AMAP);
	}
	
	/**
	 * 验证mapType 是否合法
	 * @param mapType
	 * @return
	 */
	public static boolean validateMapType(int mapType) {
		return SET_MAPTYPE.contains(mapType);
	}
	
	/**
	 * 计算 a b 两点之间的距离
	 * @param a
	 * @param b
	 * @return double
	 */
	public static double calDistance(Point a, Point b) {
		double a1 = a.getLat();
		double a2 = a.getLng();
		double b1 = b.getLat();
		double b2 = b.getLng();
		double lat1 = a1 / 180 * Math.PI;
		double lon1 = a2 / 180 * Math.PI;
		double lat2 = b1 / 180 * Math.PI;
		double lon2 = b2 / 180 * Math.PI;
		double dlat = lat2 - lat1;
		double dlon = lon2 - lon1;
		double tmpA = Math.sin(dlat / 2) * Math.sin(dlat / 2) + Math.cos(lat1)
				* Math.cos(lat2) * Math.sin(dlon / 2) * Math.sin(dlon / 2);
		double tempC = 2.0 * Math.atan2(Math.sqrt(tmpA), Math.sqrt(1.0 - tmpA));
		return 6371000 * tempC;
	}
}
