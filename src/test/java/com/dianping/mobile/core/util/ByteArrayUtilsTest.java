package com.dianping.mobile.core.util;

import java.util.Random;

import org.junit.Test;

public class ByteArrayUtilsTest {

	@Test
	public void test(){
		int[] lens = { 24000, 40000, 46000, 54000, 120000, 210000 };
		Random rnd = new Random(System.currentTimeMillis());
		for (int len : lens) {
			byte[] bytes = new byte[len];
			rnd.nextBytes(bytes);
			String[] arr = ByteArrayUtils.encode(bytes);
			byte[] bytes2 = ByteArrayUtils.decode(arr);

//			if (!ByteArrayUtils.base64Encode(bytes, bytes.length).equals(
//					ByteArrayUtils.base64Encode(bytes2, bytes2.length))) {
//				System.err.println("ERROR");
//				return;
//			}
		}
	}

}
