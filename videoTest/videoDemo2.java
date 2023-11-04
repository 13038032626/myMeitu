package videoTest;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.InflaterInputStream;

public class videoDemo2 {
    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\吴松林\\IdeaProjects\\meitu2\\压缩中的压缩.dat");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); //此输出流中写入所有信息，最后转出为byte[]，类似桶子
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,bytesRead);
        }
        byte[] data = outputStream.toByteArray();

        InflaterInputStream iutputStream1 = utils.inflaterCompressedBytes(data); //解压
        BufferedInputStream bis = new BufferedInputStream(iutputStream1);
        List<BufferedImage> bufferedImages = new ArrayList<>();
        byte[] eachImage = new byte[(int) (20+640*480*1.75)];
        int testIndex = 0;
        int index;
        System.out.println("length: "+eachImage.length);
        try {
            while ((index = bis.read(eachImage)) != -1) {
                System.out.println("本次读取长度：" + index);
                testIndex++;
                System.out.println("test: " + testIndex);
                BufferedImage bfi = utils.getBfi(eachImage);
                bufferedImages.add(bfi);
            }
        }catch (Exception e){
            System.out.println("跳过异常，省略最后一张图片");
            e.printStackTrace();
        }
        bis.close();
        iutputStream1.close();
        outputStream.close();
        fileInputStream.close();

        JFrame jFrame = new JFrame();
        myPanel panel = new myPanel();
        jFrame.add(panel);
        jFrame.setSize(new Dimension(640,480));
        jFrame.setVisible(true);

        panel.list = bufferedImages;
        while (true){
            panel.repaint();
        }

    }
}
class myPanel extends JPanel{

    int index = 0;

    List<BufferedImage> list;
    @Override
    public void paint(Graphics g) {
        g.drawImage(list.get(index), 0, 0, null);
        if (index < list.size() - 2) {
            index++;
        }
        try {
            Thread.sleep(34);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
