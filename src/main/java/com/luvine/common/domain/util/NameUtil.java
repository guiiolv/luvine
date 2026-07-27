package com.luvine.common.domain.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class NameUtil {

    private NameUtil() {}

    public static String normalize(String value) {
        String[] words = value.trim().toLowerCase().split("\\s+");

        return Arrays
                .stream(words)
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}