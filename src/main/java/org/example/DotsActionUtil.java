package org.example;

import java.io.File;

public class DotsActionUtil {

    public static String replaceAllDots(String str) {
        StringBuilder builder = new StringBuilder();
        for (char symbol : str.toCharArray()) {
            if (symbol == '.') {
                builder.append(File.separator);
            } else {
                builder.append(symbol);
            }
        }
        return builder.toString();
    }

    public static String placeAllDots(String str) {
        String regExpSeparator = "\\".equals(File.separator) ? "\\\\" : File.separator;
        return str.replaceAll(regExpSeparator, ".");
    }

}
