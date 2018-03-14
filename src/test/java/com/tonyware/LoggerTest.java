package com.tonyware;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoggerTest {

    @Test
    public void simpleTest() throws IOException {
        String filename = "/tmp/testLog";
        Path path = Paths.get(filename);

        if (Files.deleteIfExists(path));

        Logger logger = new Logger("name", filename);

        logger.info("first");
        logger.debug("second");
        logger.error("third");

        Assertions.assertEquals(3, Files.lines(path).count());
    }

    @Test
    public void exceptionTest() throws IOException {
        String filename = "/tmp/testLog";
        Path path = Paths.get(filename);

        if (Files.deleteIfExists(path));

        Logger logger = new Logger("name", filename);

        logger.error(new RuntimeException());

        logger.error("just another exception", new IllegalArgumentException());
    }

}
