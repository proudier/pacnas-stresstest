package net.pierreroudier.pacnas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Measure {
    private final Logger logger = LoggerFactory.getLogger(StresstestVerticle.class);

    long startTimestamp;
    long endTimestamp;

    void printTimeLaps(int id) {
        long deltaNanosec = endTimestamp - startTimestamp;
        long deltaMicrosec = deltaNanosec / 1000;
        long deltaMillisec = deltaNanosec / 1000000;

        logger.info("[{}] {} ms", id, deltaMillisec);
    }
}
