/**
 *
 */
package com.dianping.mobile.core.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author kewen.yao
 */
public final class VersionUtil {

    private VersionUtil() {
    }

    /**
     * return 1,versionA > versionB
     * return 0,versionA = versionB
     * return -1,versionA < versionB
     *
     * @param versionA
     * @param versionB
     * @return
     */
    public static int compare(String versionA, String versionB) {
        int[] versionASplit = strToArray(versionA);
        int[] versionBSplit = strToArray(versionB);
        if (versionASplit.length != versionBSplit.length) {
            if (versionASplit.length > versionBSplit.length) {
                for (int l = 0; l < versionBSplit.length; l++) {
                    if (versionASplit[l] > versionBSplit[l]) {
                        return 1;
                    } else if (versionASplit[l] < versionBSplit[l]) {
                        return -1;
                    } else {
                        continue;
                    }
                }
                for (int i = versionASplit.length - 1; i >= versionBSplit.length; i--) {
                    if (versionASplit[i] != 0) {
                        return 1;
                    }
                }
                return 0;
            } else {
                for (int h = 0; h < versionASplit.length; h++) {
                    if (versionASplit[h] > versionBSplit[h]) {
                        return 1;
                    } else if (versionASplit[h] < versionBSplit[h]) {
                        return -1;
                    } else {
                        continue;
                    }
                }
                for (int j = versionBSplit.length - 1; j >= versionASplit.length; j--) {
                    if (versionBSplit[j] != 0) {
                        return -1;
                    }
                }
                return 0;
            }
        } else {
            for (int k = 0; k < versionASplit.length; k++) {
                if (versionASplit[k] > versionBSplit[k]) {
                    return 1;
                } else if (versionASplit[k] < versionBSplit[k]) {
                    return -1;
                } else {
                    continue;
                }
            }
            return 0;
        }
    }

    private static int[] strToArray(String version) {
        if (!StringUtils.isBlank(version)) {
            String[] s = version.split("\\.");
            int[] versionNum = new int[s.length];
            for (int i = 0; i < s.length; i++) {
                int num = Integer.parseInt(s[i]);
                if (num < 0) {
                    throw new IllegalArgumentException("version num < 0");
                } else {
                    versionNum[i] = num;
                }
            }
            return versionNum;
        } else {
            throw new IllegalArgumentException("versionStr blank");
        }
    }
}
