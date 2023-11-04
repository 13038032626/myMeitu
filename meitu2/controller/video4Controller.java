package com.example.meitu2.controller;

import com.example.meitu2.utils.Huffman;
import com.example.meitu2.utils.outerCompressionUtils;
import com.github.sarxos.webcam.Webcam;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class video4Controller {

    @Autowired
    Webcam webcam;

    @GetMapping("/compressedVideos")
    public void getCompressedBytes() throws IOException {

        webcam.open();
        long startTime = System.currentTimeMillis();
        List<BufferedImage> bufferedImages = new ArrayList<>();
        while (System.currentTimeMillis() - startTime < 5000) {
            BufferedImage image = webcam.getImage();
            bufferedImages.add(image);
        }
        System.out.println("录制结束");
        webcam.close();
        byte[] bytes = outerCompressionUtils.photosToCompressedBytes(bufferedImages);
        File file = new File("压缩中的压缩.dat");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
        System.out.println("持久化结束");
    }
    @GetMapping("/huffmanTable")
    public Map<Byte,String> getHuffmanTable(){
        return Huffman.encodingTable;
    }

    @GetMapping("huffmanString")
    public void getHuffmanString() throws IOException {
        webcam.open();
        long startTime = System.currentTimeMillis();
        List<BufferedImage> bufferedImages = new ArrayList<>();
        while (System.currentTimeMillis() - startTime < 5000) {
            BufferedImage image = webcam.getImage();
            bufferedImages.add(image);
        }
        System.out.println("录制结束");
        webcam.close();
        byte[] bytes = outerCompressionUtils.photosToCompressedBytes(bufferedImages);
        String huffmaned = Huffman.huffmanEncoding(bytes);
        File file = new File("压缩中的压缩中的压缩.txt");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(huffmaned.getBytes());
        fos.close();
        System.out.println("持久化结束");
    }
}
