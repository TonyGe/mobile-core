package com.dianping.mobile.core.util;

import java.util.ArrayList;

public final class ByteArrayUtils {

    private static final int BLOCK_SIZE = 46000;
    private final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
            .toCharArray();
    private static int[] toInt = new int[128];

    static {
        for (int i = 0; i < ALPHABET.length; i++) {
            toInt[ALPHABET[i]] = i;
        }
    }

    private ByteArrayUtils() {
    }

    public static String[] encode(byte[] bytes) {
        if (bytes.length <= BLOCK_SIZE) {
            return new String[]{base64Encode(bytes, bytes.length)};
        }
        byte[] buf = new byte[BLOCK_SIZE];
        int i = 0;
        ArrayList<String> list = new ArrayList<String>();
        while (i < bytes.length) {
            int j = bytes.length - i;
            if (j > BLOCK_SIZE) {
                j = BLOCK_SIZE;
            }
            System.arraycopy(bytes, i, buf, 0, j);
            list.add(base64Encode(buf, j));
            i += j;
        }
        return list.toArray(new String[list.size()]);
    }

    public static byte[] decode(String[] arr) {
        if (arr.length == 0) {
            return new byte[0];
        }
        if (arr.length == 1) {
            return base64Decode(arr[0]);
        }
        byte[][] barr = new byte[arr.length][];
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            byte[] buf = base64Decode(arr[i]);
            barr[i] = buf;
            len += buf.length;
        }
        byte[] bytes = new byte[len];
        int i = 0;
        for (byte[] buf : barr) {
            System.arraycopy(buf, 0, bytes, i, buf.length);
            i += buf.length;
        }
        return bytes;
    }

    /**
     * Translates the specified byte array into Base64 string.
     *
     * @param buf the byte array (not null)
     * @return the translated Base64 string (not null)
     */
    private static String base64Encode(byte[] buf, int size) {
        char[] ar = new char[((size + 2) / 3) * 4];
        int a = 0;
        int i = 0;
        while (i < size) {
            byte b0 = buf[i++];
            byte b1 = (i < size) ? buf[i++] : 0;
            byte b2 = (i < size) ? buf[i++] : 0;

            int mask = 0x3F;
            ar[a++] = ALPHABET[(b0 >> 2) & mask];
            ar[a++] = ALPHABET[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
            ar[a++] = ALPHABET[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
            ar[a++] = ALPHABET[b2 & mask];
        }
        switch (size % 3) {
            case 1:
                ar[--a] = '=';
            case 2:
                ar[--a] = '=';
        }
        return new String(ar);
    }

    /**
     * Translates the specified Base64 string into a byte array.
     *
     * @param s the Base64 string (not null)
     * @return the byte array (not null)
     */
    private static byte[] base64Decode(String s) {
        int delta = s.endsWith("==") ? 2 : s.endsWith("=") ? 1 : 0;
        byte[] buffer = new byte[s.length() * 3 / 4 - delta];
        int mask = 0xFF;
        int index = 0;
        for (int i = 0; i < s.length(); i += 4) {
            int c0 = toInt[s.charAt(i)];
            int c1 = toInt[s.charAt(i + 1)];
            buffer[index++] = (byte) (((c0 << 2) | (c1 >> 4)) & mask);
            if (index >= buffer.length) {
                return buffer;
            }
            int c2 = toInt[s.charAt(i + 2)];
            buffer[index++] = (byte) (((c1 << 4) | (c2 >> 2)) & mask);
            if (index >= buffer.length) {
                return buffer;
            }
            int c3 = toInt[s.charAt(i + 3)];
            buffer[index++] = (byte) (((c2 << 6) | c3) & mask);
        }
        return buffer;
    }

}
