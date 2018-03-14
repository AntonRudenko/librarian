package com.tonyware;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


public class Logger {

    private static final String ERROR_EXTENDED = "_error_extended";
    private static final char START_PAD_CHAR = '>';
    private static final char END_PAD_CHAR = '<';
    private static final int LINE_SIZE = 121;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    private String name;
    private String filename;
    private boolean splitExceptions; // TODO param
    private boolean surroundExceptions = true; // TODO

    private boolean stripExceptions = true;
    private List<String> exceptionPackagesToInclude = Arrays.asList("com.tonyware");


    private BufferedWriter writer;
    private BufferedWriter extendedExceptionWriter;

    @SneakyThrows
    Logger(String name, @NonNull String filename) {
        this(name, filename, true);
    }

    @SneakyThrows
    Logger(String name, @NonNull String filename, Boolean splitExceptions) {
        this.name = name;
        this.filename = filename;
        this.splitExceptions = splitExceptions;

        writer = createWriter(filename);
        if (splitExceptions) extendedExceptionWriter = createWriter(addPostfix(filename, ERROR_EXTENDED));
    }

    private BufferedWriter createWriter(String filename) throws IOException {
        Path path = Paths.get(filename);

        if (Files.notExists(path)) {
            Files.createFile(path);
        }

        return Files.newBufferedWriter(path, StandardOpenOption.APPEND);
    }

    private String addPostfix(String filename, String postfix) {
        int dotPosition = filename.lastIndexOf('.');

        return dotPosition != -1
                ? filename.substring(0, dotPosition) + postfix + filename.substring(dotPosition)
                : filename + postfix;
    }


    @SneakyThrows
    public void info(String message) {
        line(message);
    }

    @SneakyThrows
    public void debug(String message) {
        line(message);
    }

    @SneakyThrows
    public void error(String message) {
        line(message);
    }

    @SneakyThrows
    public void error(Throwable t) {
        printStacktrace(t);
    }

    private String exceptionToString(Throwable t) {
        if (stripExceptions) {
            String[] stackFrames = ExceptionUtils.getStackFrames(t);

            StringBuilder sb = new StringBuilder();
            sb.append(stackFrames[0]);
            sb.append("\n");

            String stackFrame;
            for (int i = 1; i < stackFrames.length - 1; i++) {
                stackFrame = stackFrames[i];

                for (String pkg : exceptionPackagesToInclude) {
                    if (stackFrame.contains(pkg)) {
                        sb.append(stackFrame);
                        sb.append("\n");
                        continue;
                    }
                }
            }

            sb.append(stackFrames[stackFrames.length - 1]);
            return sb.toString();
        } else {
            return ExceptionUtils.getStackTrace(t);
        }
    }

    private void printStacktrace(Throwable t) throws IOException {
        if (splitExceptions) {
            String createdUuid = printUidBoxedMessage(t.getMessage(), writer, null);
            printUidBoxedMessage(exceptionToString(t), extendedExceptionWriter, createdUuid);
        } else {
            printUidBoxedMessage(exceptionToString(t), writer, null);
        }
    }

    private String printUidBoxedMessage(String message, BufferedWriter writer, String uuid) throws IOException {
        if (StringUtils.isEmpty(uuid)) uuid = UUID.randomUUID().toString();

        line(StringUtils.center(uuid, 121, START_PAD_CHAR), writer, false);
        line(message, writer, true);
        line(StringUtils.center(uuid, LINE_SIZE, END_PAD_CHAR), writer, false);

        return uuid;
    }

    @SneakyThrows
    public void error(String message, Throwable t) {
        line(message);
        error(t);
    }

    private void line(String message) throws IOException {
        line(message, writer, true);
    }

    private void line(String message, BufferedWriter writer, boolean withDate) throws IOException {
        if (withDate) {
            writer.write(currentDate());
            writer.write(": ");
        }

        if (StringUtils.isEmpty(message)) message = "null";

        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    private String currentDate() {
        return dateFormat.format(new Date());
    }

    public static final class LoggerBuilder {
        private String name;
        private String filename;
        private boolean splitExceptions; // TODO param
        private boolean surroundExceptions = true; // TODO
        private List<String> exceptionPackagesToInclude = Arrays.asList("com.tonyware");

        private LoggerBuilder() {
        }

        public static LoggerBuilder aLogger() {
            return new LoggerBuilder();
        }

        public LoggerBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public LoggerBuilder withFilename(String filename) {
            this.filename = filename;
            return this;
        }

        public LoggerBuilder withSplitExceptions(boolean splitExceptions) {
            this.splitExceptions = splitExceptions;
            return this;
        }

        public LoggerBuilder withSurroundExceptions(boolean surroundExceptions) {
            this.surroundExceptions = surroundExceptions;
            return this;
        }

        public LoggerBuilder withExceptionPackagesToInclude(List<String> exceptionPackagesToInclude) {
            this.exceptionPackagesToInclude = exceptionPackagesToInclude;
            return this;
        }

        public Logger build() {
            Logger logger = new Logger(name, filename);
            logger.exceptionPackagesToInclude = this.exceptionPackagesToInclude;
            logger.stripExceptions = exceptionPackagesToInclude.size() > 0;
            logger.splitExceptions = this.splitExceptions;
            logger.surroundExceptions = this.surroundExceptions;
            return logger;
        }
    }
}

