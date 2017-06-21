/**
 *
 */
package com.dianping.mobile.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author kewen.yao
 */
public final class ApiUtil {
    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private static String ip;

    private ApiUtil() {
    }

    /**
     * parse file to string array, the whole file will be stored in memory, so
     * it should not be too large
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static List<String> parseFile(String fileName) throws IOException {
        InputStream is = ApiUtil.class.getResourceAsStream(fileName);
        return parseFile(is);
    }

    public static List<String> parseFile(InputStream is) throws IOException {
        List<String> result = new ArrayList<String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(is));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                result.add(s);
            }
            bufferedReader.close();
            return result;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).find();
    }

    public static String unicodeToChinese(String unicode) {
        if (unicode == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        int start = 0, end = 0;
        try {
            while (true) {
                end = unicode.indexOf("\\u", start);
                if (end != -1) {
                    buffer.append(unicode.substring(start, end));
                } else {
                    buffer.append(unicode.substring(start));
                    break;
                }
                char c = (char) Integer.parseInt(
                        unicode.substring(end + 2, end + 6), 16);
                buffer.append(new Character(c).toString());
                start = end + 6;
            }
        } catch (StringIndexOutOfBoundsException ex) {
            return null;
        }
        return buffer.toString();
    }

    public static int getHash16(String s) {
        int i = s.hashCode();
        return (0xFFFF & i) ^ (i >>> 16);
    }

    public static String getIp() {
        if (ip != null) {
            return ip;
        }
        try {
            ip = IpUtil.getFirstNoLoopbackAddress();
        } catch (Exception e) {
            ip = "127.0.0.1";
        }
        return ip;
    }

}
