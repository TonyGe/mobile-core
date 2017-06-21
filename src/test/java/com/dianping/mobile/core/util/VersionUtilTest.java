/**
 *
 */
package com.dianping.mobile.core.util;

import junit.framework.Assert;
import org.junit.Test;


/**
 * @author kewen.yao
 */
public class VersionUtilTest {
    @Test
    public void test() {
        Assert.assertEquals(VersionUtil.compare("4.1.2", "3.1.2"), 1);
        Assert.assertEquals(VersionUtil.compare("3.1.2", "4.1.2"), -1);
        Assert.assertEquals(VersionUtil.compare("4.1.2", "4.0.2"), 1);
        Assert.assertEquals(VersionUtil.compare("4.1.2", "4.2.2"), -1);
        Assert.assertEquals(VersionUtil.compare("4.1.2", "4.1.1"), 1);
        Assert.assertEquals(VersionUtil.compare("4.1.2", "4.1.3"), -1);
        Assert.assertEquals(VersionUtil.compare("4.1.2", "4.1.2"), 0);
        Assert.assertEquals(VersionUtil.compare("4.1", "4.1.0"), 0);
        Assert.assertEquals(VersionUtil.compare("4.1", "4.1.2"), -1);
        Assert.assertEquals(VersionUtil.compare("4.1.2", "4.1"), 1);
        Assert.assertEquals(VersionUtil.compare("4.1.2", "4.2"), -1);
    }
}
