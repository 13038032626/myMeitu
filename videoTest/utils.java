package videoTest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class utils {
    public static InflaterInputStream inflaterCompressedBytes(byte[] bytes) throws IOException {
        //解压数据
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        InflaterInputStream lis = new InflaterInputStream(bais, new Inflater());

        return lis;
    }

    public static BufferedImage getBfi(byte[] originalBytes) {
        byte one = originalBytes[0];
        byte two = originalBytes[1];
        byte three = originalBytes[2];
        byte four = originalBytes[3];

        int Y = ((one & 0xff) << 24) | ((two & 0xff) << 16) | ((three & 0xff) << 8) | (four & 0xff);

        byte one2 = originalBytes[4];
        byte two2 = originalBytes[5];
        byte three2 = originalBytes[6];
        byte four2 = originalBytes[7];

        int U = ((one2 & 0xff) << 24) | ((two2 & 0xff) << 16) | ((three2 & 0xff) << 8) | (four2 & 0xff);
        byte one3 = originalBytes[8];
        byte two3 = originalBytes[9];
        byte three3 = originalBytes[10];
        byte four3 = originalBytes[11];

        int V = ((one3 & 0xff) << 24) | ((two3 & 0xff) << 16) | ((three3 & 0xff) << 8) | (four3 & 0xff);

        byte one4 = originalBytes[12];
        byte two4 = originalBytes[13];
        byte three4 = originalBytes[14];
        byte four4 = originalBytes[15];

        int height = ((one4 & 0xff) << 24) | ((two4 & 0xff) << 16) | ((three4 & 0xff) << 8) | (four4 & 0xff);

        byte one5 = originalBytes[16];
        byte two5 = originalBytes[17];
        byte three5 = originalBytes[18];
        byte four5 = originalBytes[19];

        int width = ((one5 & 0xff) << 24) | ((two5 & 0xff) << 16) | ((three5 & 0xff) << 8) | (four5 & 0xff);

        System.out.println("Y: " + Y);
        byte[] Ys = Arrays.copyOfRange(originalBytes, 20, Y + 20);
        byte[] Us = Arrays.copyOfRange(originalBytes, Y + 20, Y + U + 20);
        byte[] Vs = Arrays.copyOfRange(originalBytes, Y + U + 20, Y + U + V + 20);

        BufferedImage bfi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int hongW = width / 2;
        int hongH = height / 2;

        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                int H = i / 2;
                int W = j / 2;
                byte y = Ys[(i / 2 * 2) * width + j / 2 * 4 + (i % 2) * 2 + j % 2];
                byte u = Us[H * hongW * 2 + j / 2 * 2 + i % 2];
                byte v = Vs[H * hongW + W];

                int r = (int) (y + 128 + 1.14075 * (v));
                int g = (int) (y + 128 - 0.3455 * (u) - 0.7169 * (v));
                int b = (int) (y + 128 + 1.779 * (u));

                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));

                int color = (r) << 16 | (g) << 8 | b;
                if (i < 1 && j < 20) {
//                    System.out.println("YUV: "+y+" "+u+" "+v);
//                    System.out.println("rgb: i: " + i + "  j:" + j + "  " + r + " " + g + " " + b);
                }
                bfi.setRGB(j, i, color);
            }
        }
        return bfi;
    }

    public static byte[] deCodingHuffman(String enCodedData, Map<Byte, String> encodingMap) {
        ArrayList<Byte> decodedData = new ArrayList<>();
        String currentCode = "";
        for (char bit : enCodedData.toCharArray()
        ) {
            currentCode += bit;
            for (Map.Entry<Byte,String> entry:encodingMap.entrySet()
                 ) {
                if(entry.getValue().equals(currentCode)){
                    decodedData.add(entry.getKey());
                    currentCode = "";
                    break;
                }
            }
        }
        byte[] bytedata = new byte[decodedData.size()];
        for (int i = 0; i < decodedData.size(); i++) {
            bytedata[i] = decodedData.get(i);
        }
        return bytedata;
    }
}
