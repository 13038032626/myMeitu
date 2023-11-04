package com.example.meitu2.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class outerCompressionUtils {

    public static byte[] photosToCompressedBytes(List<BufferedImage> bufferedImages) throws IOException {

        //数据流中未必要有各种辅助信息，比如各类字段长度，在外规定好算了
        //这里每一帧的长度就是：20 + 640 * 480 * 1.75
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeflaterOutputStream dos = new DeflaterOutputStream(baos,new Deflater());

        for (BufferedImage bufferedImage: bufferedImages
             ) {
            byte[] bytes = innerCompressionUtils.compressToOneChannel(bufferedImage);
            System.out.println("一帧的长度为："+bytes.length);
            dos.write(bytes);
        }
        byte[] compressedData = baos.toByteArray();
        return compressedData;
    }

    public static ByteArrayOutputStream inflaterCompressedBytes(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InflaterInputStream lis = new InflaterInputStream(bais,new Inflater());
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = lis.read(buffer))!=-1){
            baos.write(buffer,0,bytesRead);
        }
        baos.close();
        lis.close();

        return baos;
    }
}
