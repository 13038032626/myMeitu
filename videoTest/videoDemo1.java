package videoTest;

import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Map;

public class videoDemo1 {

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\吴松林\\IdeaProjects\\meitu2\\写入测试压缩版本.dat");
        InputStream inputStream = new FileInputStream(file);

        byte[] bytes = new byte[20];
        inputStream.read(bytes);

        byte one = bytes[0];
        byte two = bytes[1];
        byte three = bytes[2];
        byte four = bytes[3];

        int Y = ((one & 0xff) << 24) | ((two & 0xff) << 16) | ((three & 0xff) << 8) | (four & 0xff);

        byte one2 = bytes[4];
        byte two2 = bytes[5];
        byte three2 = bytes[6];
        byte four2 = bytes[7];

        int U = ((one2 & 0xff) << 24) | ((two2 & 0xff) << 16) | ((three2 & 0xff) << 8) | (four2 & 0xff);
        byte one3 = bytes[8];
        byte two3 = bytes[9];
        byte three3 = bytes[10];
        byte four3 = bytes[11];

        int V = ((one3 & 0xff) << 24) | ((two3 & 0xff) << 16) | ((three3 & 0xff) << 8) | (four3 & 0xff);

        byte one4 = bytes[12];
        byte two4 = bytes[13];
        byte three4 = bytes[14];
        byte four4 = bytes[15];

        int height = ((one4 & 0xff) << 24) | ((two4 & 0xff) << 16) | ((three4 & 0xff) << 8) | (four4 & 0xff);

        byte one5 = bytes[16];
        byte two5 = bytes[17];
        byte three5 = bytes[18];
        byte four5 = bytes[19];

        int width = ((one5 & 0xff) << 24) | ((two5 & 0xff) << 16) | ((three5 & 0xff) << 8) | (four5 & 0xff);
        System.out.println("Y: " + Y);
        System.out.println("U: " + U);
        System.out.println("V: " + V);
        System.out.println("height: " + height);
        System.out.println("width: " + width);

        byte[] total = new byte[Y + U + V];
        int start = 0;
        while (inputStream.available() != 0) {
            if (inputStream.available() >= 1024) {
                int read = inputStream.read(total, start, 1024);
                start += read;
            } else {
                int read = inputStream.read(total, start, inputStream.available());
                start += read;
            }
        }
        byte[] Ys = Arrays.copyOfRange(total, 0, Y);
        byte[] Us = Arrays.copyOfRange(total, Y, Y + U);
        byte[] Vs = Arrays.copyOfRange(total, Y + U, Y + U + V);

        for (int i = 0; i < 10; i++) {
            System.out.println("Ys:" + Ys[i]);
        }
        for (int i = 0; i < 100; i++) {
            System.out.println("Us:" + i + "  " + Us[i]);
        }
        for (int i = 0; i < 100; i++) {
            System.out.println("Vs:" + i + "  " + Vs[i]);
        }
        int targetYs = 0;
        int targetUs = 0;
        int targetVs = 0;

        BufferedImage bfi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int hongW = width / 2;
        int hongH = height / 2;

        for (int i = 0; i < height-1; i++) {
            for (int j = 0; j < width-1; j++) {
                int H = i / 2;
                int W = j / 2;
                byte y = Ys[(i/2*2) * width + j / 2 * 4 + (i % 2) * 2 + j % 2];
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
                    System.out.println("rgb: i: " + i + "  j:" + j + "  " + r + " " + g + " " + b);
                }
                bfi.setRGB(j, i, color);
            }
        }
        JFrame jFrame = new JFrame();
        JPanel jp = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.drawImage(bfi, 0, 0, null);
            }
        };
        jFrame.add(jp);
        jFrame.setSize(new Dimension(4000, 3000));
        jFrame.setVisible(true);
    }
}

