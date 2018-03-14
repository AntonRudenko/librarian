package com.tonyware;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LoggerRepository {

    private static Map<String, Logger> loggers = new HashMap<>();

    public static Logger createLogger(String name, String filename) {
        Logger logger = loggers.get(name);

        if (logger != null) throw new IllegalArgumentException("Logger with name " + name +  " already exists");

        // check logger with same filename
        Optional<Logger> loggerWithSameFile = loggers.values().stream().filter(l -> l.getFilename().equals(filename)).findAny();
        if (loggerWithSameFile.isPresent()) {
            throw new IllegalArgumentException("Logger with same filename " + filename +  " already exists");
        }

        logger = new Logger(name, filename);

        loggers.put(name, logger);

        return logger;
    }

    public static Logger getLogger(String name) {
        Logger logger = loggers.get(name);
        if (logger == null) throw new IllegalArgumentException("Logger with name " + name + " does not exist");
        return logger;
    }

}
