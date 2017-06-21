package com.dianping.mobile.core.featurecontigency;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.*;

public final class FeatureContingency {

    private static final String FC_CONFIG_PATH = "classpath:/config/fc/FeatureContingency.xml";
    private static Logger log = Logger.getLogger(FeatureContingency.class);


    private static Map<Integer, FC> fcMap = new HashMap<Integer, FC>();

    static {
        loadConfiguration();
    }

    private FeatureContingency() {
    }

    private static void loadConfiguration() {
        try {
            ResourceLoader resLoader = new DefaultResourceLoader();
            Resource resource = resLoader.getResource(FC_CONFIG_PATH);
            Digester digester = new Digester();
            digester.addObjectCreate("FeatureContingencySet", ArrayList.class);
            digester.addObjectCreate(
                    "FeatureContingencySet/FeatureContingency", FC.class);
            digester
                    .addSetProperties("FeatureContingencySet/FeatureContingency");
            digester.addSetNext("FeatureContingencySet/FeatureContingency",
                    "add", FC.class.getName());
            List<?> fcList = (List<?>) digester
                    .parse(resource.getInputStream());
            if (fcList != null && fcList.size() > 0) {
                HashMap<Integer, FC> tmpMap = new HashMap<Integer, FC>();

                for (int i = 0; i < fcList.size(); ++i) {
                    FC fc = (FC) fcList.get(i);
                    if (fc != null) {
                        tmpMap.put(fc.id, fc);
                    }
                }
                fcMap = tmpMap;
            }
        } catch (Exception e) {
            log.error("FeatureContingency failed to initialize", e);
        }
    }

    public static boolean isOn(int fcId) {
        FC fc = fcMap.get(fcId);
        if (fc != null) {
            return fc.getStatus() == FC.STATUS_ON;
        } else {
            return false;
        }
    }

    /**
     * Wire on FC until server restart
     *
     * @param fcId
     */
    public static void wireOn(int fcId) {
        operate(fcId, true);
    }

    /**
     * Wire off FC until server restart
     *
     * @param fcId
     */
    public static void wireOff(int fcId) {
        operate(fcId, false);
    }

    private static void operate(int fcId, boolean wireOn) {
        FC fc = fcMap.get(fcId);
        if (fc != null) {
            fc.setStatus(wireOn ? FC.STATUS_ON : FC.STATUS_OFF);
        }
    }

    public static String echo() {
        Iterator<FC> it = fcMap.values().iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("FeatureContingecy status:\nid\t\tname\t\tstatus\n");
        while (it.hasNext()) {
            FC fc = it.next();
            sb.append(String.format("%s\t\t%s\t\t%s\n", fc.id, fc.name,
                    fc.status));
        }

        return sb.toString();
    }

    public static class FC {

        public static final int STATUS_ON = 0;
        public static final int STATUS_OFF = 1;
        public static final int STATUS_DELETE = 2;

        private int id;
        private int status;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}