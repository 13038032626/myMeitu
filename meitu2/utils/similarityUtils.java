package com.example.meitu2.utils;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class similarityUtils {

    public int getMeanSquareError(BufferedImage image1, BufferedImage image2) {

        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
            return -1;
        }
        int height = image1.getHeight();
        int width = image1.getWidth();
        long total = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb1 = image1.getRGB(j, i);
                int rgb2 = image2.getRGB(j, i);
                total += Math.abs(rgb1 - rgb2);
            }
        }
        return (int) (total/(height*width));
    }
}


