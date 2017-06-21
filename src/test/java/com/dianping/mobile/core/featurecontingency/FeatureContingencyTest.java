package com.dianping.mobile.core.featurecontingency;

import com.dianping.mobile.core.featurecontigency.FeatureContingency;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FeatureContingencyTest {

    @Test
    public void test() {
        //normal case
        assertEquals(false, FeatureContingency.isOn(0));
        FeatureContingency.wireOn(0);
        assertEquals(true, FeatureContingency.isOn(0));
        FeatureContingency.wireOn(0);
        StringBuffer buffer = new StringBuffer("FeatureContingecy status:\n");
        buffer.append("id\t\tname\t\tstatus\n");
        buffer.append("0\t\tSampleFC\t\t0\n");
        assertEquals(buffer.toString(), FeatureContingency.echo());

        assertEquals(true, FeatureContingency.isOn(0));
        FeatureContingency.wireOff(0);
        assertEquals(false, FeatureContingency.isOn(0));
        FeatureContingency.wireOff(0);
        assertEquals(false, FeatureContingency.isOn(0));

        buffer = new StringBuffer("FeatureContingecy status:\n");
        buffer.append("id\t\tname\t\tstatus\n");
        buffer.append("0\t\tSampleFC\t\t1\n");
        assertEquals(buffer.toString(), FeatureContingency.echo());

        //abnormal case
        assertEquals(false, FeatureContingency.isOn(-100));
        FeatureContingency.wireOn(-100);
        assertEquals(false, FeatureContingency.isOn(-100));
    }

}
