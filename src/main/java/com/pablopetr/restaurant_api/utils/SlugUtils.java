package com.pablopetr.restaurant_api.utils;

import java.text.Normalizer;

public class SlugUtils {

    public static String toSlug(String input) {
        if (input == null) {
            return null;
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String lowercased = normalized.toLowerCase();

        return lowercased.replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
