package com.apm.demo.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class MemoryException {
    private static final Logger log = LoggerFactory.getLogger(FlightSimulator.class);

    private static HashMap<Object, Object> myMap = new HashMap<>();

    public static void start() throws Exception {

        createObjects();
    }

    public static void stop() {
        log.info("Memory leak problem terminated!");
    }

    private static boolean flag = true;

    public static void setFlag(boolean newValue) {
        flag = newValue;
    }

    public static void createObjects() throws Exception {

        long counter = 0;
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        while (flag) {
            log.info("Started memory exception simulator " + counter);
            // If free memory is less than 100 mb, then keep sleeping;
            //long freeMem = Runtime.getRuntime().maxMemory();
            /**if (counter % 1000 == 0) {
             System.out.println("max: " + Runtime.getRuntime().maxMemory() / (1024 * 1024)
             + " (mb), total: "
             + Runtime.getRuntime().totalMemory() / (1024 * 1024)
             + " (mb), free: "
             + Runtime.getRuntime().freeMemory() / (1024 * 1024) );
             }*/
            long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            log.info(afterUsedMem-beforeUsedMem+" > "+100*1024*1024);
            if ( ((afterUsedMem-beforeUsedMem) > 100 * 1024 * 1024)) {
                Thread.sleep(10000);
                System.out.println("sleeping!");
                System.out.println("max: " + Runtime.getRuntime().maxMemory() / (1024 * 1024)
                        + " (mb), total: "
                        + Runtime.getRuntime().totalMemory() / (1024 * 1024)
                        + " (mb), free: "
                        + Runtime.getRuntime().freeMemory() / (1024 * 1024) );
                myMap = new HashMap<>();
                Runtime.getRuntime().gc();
                break;
            }

            myMap.put("key" + counter, "Large stringgggggggggggggggggggggggggggg"
                    + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                    + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                    + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                    + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                    + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                    + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                    + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                    + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                    + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                    + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                    + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                    + counter);
            ++counter;

            if (counter % 1000 == 0) {

                System.out.println("Inserted 1000 Records to map!");
            }
        }
    }
}
