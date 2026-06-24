package cz.cvut.fel.pjv.engine.io;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configures the logging system.
 */
public class LogConfig {
    private static final Logger LOGGER = Logger.getLogger("SoulMine");

    public static void setup(boolean enableLogs) {
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();

        if (!enableLogs) {
            for (Handler h : handlers) {
                h.setLevel(Level.OFF);
            }
        } else {
            for (Handler h : handlers) {
                h.setLevel(Level.ALL);
            }
            LOGGER.info("Logs are ENABLED");
        }
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}