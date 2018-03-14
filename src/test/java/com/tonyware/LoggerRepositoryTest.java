package com.tonyware;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class LoggerRepositoryTest {

    @Test()
    public void mainTest() {
        String property = System.getProperty("java.io.tmpdir");

        Path path = Paths.get(property, UUID.randomUUID().toString() + ".log");

        Logger testLogger = LoggerRepository.createLogger("mainLogger", path.toString());

        testLogger.debug("test");

    }

    @Test()
    public void testSameName() {
        String property = System.getProperty("java.io.tmpdir");

        Path path = Paths.get(property, UUID.randomUUID().toString() + ".log");

        Logger testLogger = LoggerRepository.createLogger("testLogger", path.toString());
        assertThrows(IllegalArgumentException.class, () -> LoggerRepository.createLogger("testLogger", path.toString()));
    }

    @Test
    public void testSameFile() {
        String property = System.getProperty("java.io.tmpdir");

        Path path = Paths.get(property, UUID.randomUUID().toString() + ".log");

        Logger testLogger = LoggerRepository.createLogger("file1", path.toString());
        assertThrows(IllegalArgumentException.class, () -> LoggerRepository.createLogger("file2", path.toString()));
    }

}
