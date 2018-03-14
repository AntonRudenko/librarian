package com.tonyware;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTest {

    @Test
    public void fileTesting() throws IOException {

        Path path = Paths.get("/home/antonrudenko/temp/loggertest.log");

        BufferedWriter bufferedWriter = Files.newBufferedWriter(path);

        bufferedWriter.write("test string bur");


    }

    public static void main(String[] args) {
        Exception e = new RuntimeException();

        String[] stackFrames = ExceptionUtils.getStackFrames(e);

        System.out.println(stackFrames[0]);
        System.out.println(stackFrames[1]);


        System.out.println("--------------------------------");
        System.out.println(ExceptionUtils.getStackTrace(e));


    }

}
