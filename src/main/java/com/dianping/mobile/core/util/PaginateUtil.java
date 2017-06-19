/**
 * 
 */
package com.dianping.mobile.core.util;

/**
 * @author kewen.yao
 *
 */
public class PaginateUtil {
	
	/**
	 * 分页有两种形式 start limit 和 pageNo pageSize limit=pageSize
	 * 该方法提供从start 到 pageNo的转化
	 * @param start
	 * @param limit
	 * @return
	 */
	public static int start2PageNo(int start, int limit) {
		return (start/limit) + 1 + (start % limit > 1 ? 1 : 0);
	}
	
}
