/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fitness_ua.LocalServer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author salterok
 */
public class Templater {
    private final static String TEMPLATE_REGEX = "(\\{!(?<key>\\w+)\\})";
    private final static Pattern templatePattern = Pattern.compile(TEMPLATE_REGEX);

    private static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private static String replaceAll(String input, Dict source) {
        StringBuffer output = new StringBuffer();
        Matcher matcher = templatePattern.matcher(input);
        while (matcher.find()) {
            String temp = matcher.group("key");
            matcher.appendReplacement(output, source.get(temp));
        }
        matcher.appendTail(output);
        return output.toString();
    }
    
    public static String apply(String filename, Dict data) {
        try {
            System.out.println(filename);
            String content = readFile(filename, Charset.forName("UTF-8"));
            return replaceAll(content, data);
        }
        catch (Exception ex) {
            return null;
        }
    }
}
