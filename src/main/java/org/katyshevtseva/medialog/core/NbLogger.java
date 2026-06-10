package org.katyshevtseva.medialog.core;

import org.apache.log4j.Logger;

public class NbLogger {
    private static final Logger logger = Logger.getLogger(NbLogger.class);

    public static void log(String s) {
        logger.warn(s);
    }
}
