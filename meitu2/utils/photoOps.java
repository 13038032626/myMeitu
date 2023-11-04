package com.example.meitu2.utils;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
public class photoOps {

    public static int[][] bfiToInts(BufferedImage bfi){
        int height = bfi.getHeight();
        int width = bfi.getWidth();
        int[][] target = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                target[i][j] = bfi.getRGB(j,i);
            }
        }
        return target;
    }

    public static int[] RGBToInts(int i){
        int red = (i >> 16) & 0xff;
        int green = (i >> 8) & 0xff;
        int blue = i & 0xff;

        int[] target = new int[3];
        target[0] = red;
        target[1] = green;
        target[2] = blue;

        return target;
    }

    public static int intsToRGB(int[] i){
        int red = i[0];
        int green = i[1];
        int blue = i[2];

        int target = red<<16 | green<<8 | blue;
        return target;
    }

    public static byte[] bfiToBytes(BufferedImage bfi) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bfi, "png", baos);
        byte[] byteArray = baos.toByteArray();
        return byteArray;
    }
    public static BufferedImage inputStreamToBfi(InputStream inputStream) throws IOException {
        BufferedImage read = ImageIO.read(inputStream);
        return read;

    }
}
