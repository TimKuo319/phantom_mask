package com.example.phantom_mask.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuantityParser {

    public static int parseQuantity(String maskName) {
        Pattern pattern = Pattern.compile("\\((\\d+) per pack\\)");
        Matcher matcher = pattern.matcher(maskName);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return 1;
    }
}
