package com.example.meitu2.utils;

import org.bouncycastle.util.encoders.BufferedDecoder;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class innerCompressionUtils {

    public static int[] rgb2YUV(int rgb) {
        int[] rgb1 = photoOps.RGBToInts(rgb);
        int red = rgb1[0];
        int green = rgb1[1];
        int blue = rgb1[2];

        int Y = (int) (0.299 * red + 0.587 * green + 0.114 * blue -128); //-128 到 127
        int U = (int) (-0.1684 * red - 0.3316 * green + 0.5 * blue);//-128 到 127
        int V = (int) (0.5 * red - 0.4187 * green - 0.083 * blue); //-128 到 127

        return new int[]{Y, U, V};
    }

    public static byte[] compressToOneChannel(BufferedImage bufferedImage) {

        byte[] Ys = new byte[bufferedImage.getWidth() * bufferedImage.getHeight()];
        byte[] Us = new byte[bufferedImage.getHeight() * (bufferedImage.getWidth() / 2)];
        byte[] Vs = new byte[(bufferedImage.getWidth() / 2) * (bufferedImage.getHeight() / 2)];

        int targetYs = 0;
        int targetUs = 0;
        int targetVs = 0;

        for (int i = 0; i < bufferedImage.getHeight(); i += 2) {
            for (int j = 0; j < bufferedImage.getWidth(); j += 2) {
                for (int k = 0; k < 2; k++) {
                    for (int l = 0; l < 2; l++) {
                        int[] ints = rgb2YUV(bufferedImage.getRGB(j + l, i + k));
                        int Y = ints[0];
                        Ys[targetYs] = (byte) (Y);
                        targetYs++;
                    }
                    int[] ints = rgb2YUV(bufferedImage.getRGB(j, i + k));
                    int U = ints[1];
                    Us[targetUs] = (byte) (U);
                    targetUs++;
                }
                int[] ints = rgb2YUV(bufferedImage.getRGB(j, i));
                int V = ints[2];
                Vs[targetVs] = (byte) (V);
                targetVs++;
            }

        }
        int length1 = Ys.length; //大小估计 ： 图片3000*2000 = 6000000 不会超int范围
        int length2 = Us.length;
        int length3 = Vs.length;
//        System.out.println("length1: " + length1);
//        System.out.println("length2: " + length2);
//        System.out.println("length3: " + length3);

        byte[] targetBytes = new byte[4 * 5 + length1 + length2 + length3];
        int targetIndex = 0;

        byte[] bytes1 = intToByte(length1);
        for (byte b : bytes1) {
            targetBytes[targetIndex] = b;
            targetIndex++;
        }
        byte[] bytes2 = intToByte(length2);
        for (byte b : bytes2) {
            targetBytes[targetIndex] = b;
            targetIndex++;
        }
        byte[] bytes3 = intToByte(length3);
        for (byte b : bytes3) {
            targetBytes[targetIndex] = b;
            targetIndex++;
        }
        byte[] bytes4 = intToByte(bufferedImage.getHeight());
        for (byte b : bytes4) {
            targetBytes[targetIndex] = b;
            targetIndex++;
        }
        byte[] bytes5 = intToByte(bufferedImage.getWidth());
        for (byte b : bytes5) {
            targetBytes[targetIndex] = b;
            targetIndex++;
        }
        for (byte y : Ys) {
            targetBytes[targetIndex] = y;
            targetIndex++;
        }

        for (byte u : Us) {
            targetBytes[targetIndex] = u;

            targetIndex++;
        }

        for (byte v : Vs) {
            targetBytes[targetIndex] = v;
            targetIndex++;
        }
        return targetBytes;

    }

    public static byte[] intToByte(int num) {
        byte one = (byte) (num >> 24);
        byte two = (byte) (num >> 16);
        byte three = (byte) (num >> 8);
        byte four = (byte) (num);
        return new byte[]{one, two, three, four};
    }
}
