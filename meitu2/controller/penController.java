package com.example.meitu2.controller;

import com.example.meitu2.pojos.images;
import com.example.meitu2.utils.photoOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("picture/lvjing/pen")
public class penController {

    @Autowired
    images images;

    @GetMapping("/line")
    public byte[] line(String start, String end, int d) throws IOException {  //规定前端传来的坐标格式：20A51
        String[] startPoint = start.split("A");
        String[] endPoint = end.split("A");

        int startX = Integer.parseInt(startPoint[0]);
        int startY = Integer.parseInt(startPoint[1]);

        int endX = Integer.parseInt(endPoint[0]);
        int endY = Integer.parseInt(endPoint[1]);

        BufferedImage bfi = images.getBfi();
        int[][] ints = photoOps.bfiToInts(bfi);

        //两点式确定直线
        int k = (endY - startY) / (endX - startX);
        int b = startY - k * startX;

        BufferedImage newBfi = new BufferedImage(ints[0].length, ints.length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < bfi.getHeight(); i++) {
            for (int j = 0; j < bfi.getWidth(); j++) {
                if (Math.abs(i - k * j + b) < d / 2 && j > Math.min(startX, endX) && j < Math.max(startX, endX) && i < Math.max(startY, endY) && i > Math.min(startY, endY)) {
                    newBfi.setRGB(j, i, photoOps.intsToRGB(new int[]{0,0,0}));
                } else {
                    newBfi.setRGB(j, i, ints[i][j]);
                }
            }
        }
        byte[] bytes = photoOps.bfiToBytes(newBfi);
        File file = new File("C:\\Users\\吴松林\\IdeaProjects\\myMeitu\\src\\main\\resources\\static\\Demo2.png");
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        return photoOps.bfiToBytes(newBfi);
    }

}
