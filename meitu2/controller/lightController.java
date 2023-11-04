package com.example.meitu2.controller;


import com.example.meitu2.pojos.images;
import com.example.meitu2.utils.Result;
import com.example.meitu2.utils.photoOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/picture/lvjing/light")
public class lightController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    images images;

    int lastOpsModule = 0; //记录上次操作的类型，相同类型的操作基于同一张图片

    BufferedImage lastOpsImage; //记录上次操作的原图

    @GetMapping(value = "light")
    public byte[] light(Integer index) throws IOException {
        //调整亮度
        //index 属于0-50-100，index-50 属于-50--50
        // 乘完后范围还在0-255
        System.out.println("到达亮度调整");
        BufferedImage bfi = getLastImage(1);
        int[][] ints = photoOps.bfiToInts(bfi);

        BufferedImage newBfi = new BufferedImage(bfi.getWidth(), bfi.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < ints.length; i++) {
            for (int j = 0; j < ints[0].length; j++) {
                int[] rgb = photoOps.RGBToInts(ints[i][j]);

                for (int k = 0; k < rgb.length; k++) {

                    int newIndex = index - 50;
                    if (newIndex > 0) {
//                        rgb[k] = rgb[k] + newIndex*(255 - rgb[k])/50;
                        rgb[k] = Math.min((rgb[k] + newIndex * 2), 255);
                    } else {
//                        rgb[k] = rgb[k] +newIndex*(rgb[k])/50;
                        rgb[k] = Math.max(0, rgb[k] + newIndex * 2);
                    }
                    newBfi.setRGB(j, i, photoOps.intsToRGB(rgb));
                }
            }
        }
//        byte[] bytes = photoOps.bfiToBytes(newBfi);
//        File file = new File("C:\\Users\\吴松林\\IdeaProjects\\myMeitu\\src\\main\\resources\\static\\Demo2.png");
//        OutputStream outputStream = new FileOutputStream(file);
//        outputStream.write(bytes);
        images.getAllSeriesBfi().add(newBfi);
        return photoOps.bfiToBytes(newBfi);
    }

    @GetMapping(value = "duibidu", produces = "application/x-jpg")
    public byte[] duibidu(int index) throws IOException {
        /*
        index范围在-255到255，放到0-100似乎跨度太大了
         */
        System.out.println("到达对比度");
        BufferedImage bfi = getLastImage(2);
        BufferedImage newBfi = new BufferedImage(bfi.getWidth(), bfi.getHeight(), BufferedImage.TYPE_INT_RGB);

        int[][] ints = photoOps.bfiToInts(bfi);
        for (int i = 0; i < ints.length; i++) {
            for (int j = 0; j < ints[0].length; j++) {
                int pixel = ints[i][j];
                int[] rgb = photoOps.RGBToInts(pixel);
                for (int k = 0; k < rgb.length; k++) {
                    rgb[k] = rgb[k] + (rgb[k] - 127) * index / 255;
                    rgb[k] = Math.min(rgb[k], 255);
                    rgb[k] = Math.max(rgb[k], 0);
                }
                newBfi.setRGB(j, i, photoOps.intsToRGB(rgb));
            }
        }
//        byte[] bytes = photoOps.bfiToBytes(newBfi);
//        File file = new File("C:\\Users\\吴松林\\IdeaProjects\\myMeitu\\src\\main\\resources\\static\\Demo2.png");
//        OutputStream outputStream = new FileOutputStream(file);
//        outputStream.write(bytes);
        images.getAllSeriesBfi().add(newBfi);
        return photoOps.bfiToBytes(newBfi);
    }

    @GetMapping("gaoguang")
    public byte[] gaoguang(int index) throws IOException {
        BufferedImage bfi = getLastImage(3);
        BufferedImage newBfi = new BufferedImage(bfi.getWidth(), bfi.getHeight(), BufferedImage.TYPE_INT_RGB);
        int[][] ints = photoOps.bfiToInts(bfi);

        for (int i = 0; i < ints.length; i++) {
            for (int j = 0; j < ints[0].length; j++) {
                int pixel = ints[i][j];
                int[] rgb = photoOps.RGBToInts(pixel);
                int grey = (int) (0.299 * rgb[0] + 0.587f * rgb[1] + 0.114 * rgb[2]);

                int[] newRGB = new int[3];
                if (grey > 200) {  //高亮区域
                    for (int k = 0; k < newRGB.length; k++) {
//                        int newColor = rgb[k]*(index+50)/100; // 0.5-1.5 似乎有点大
                        int newColor = rgb[k] + rgb[k] * (index - 50) / 380; // 0.5-1.5 似乎有点大

                        if (newColor > 255) {
                            newColor = 255;
                        }
                        newRGB[k] = newColor;
                    }
                    newBfi.setRGB(j, i, photoOps.intsToRGB(newRGB));
                } else if (grey > 180) {  //过渡区
                    for (int k = 0; k < newRGB.length; k++) {
                        int newColor = rgb[k] + rgb[k] * (index - 50) / 800;
                        if (newColor > 255) {
                            newColor = 255;
                        }
                        newRGB[k] = newColor;
                    }
                    newBfi.setRGB(j, i, photoOps.intsToRGB(newRGB));
                } else {
                    newBfi.setRGB(j, i, pixel);
                }
            }
        }
        images.getAllSeriesBfi().add(newBfi);
        return photoOps.bfiToBytes(newBfi);
    }

    @GetMapping("anbu")
    public byte[] anbu(int index) throws IOException {
        BufferedImage bfi = getLastImage(4);
        BufferedImage newBfi = new BufferedImage(bfi.getWidth(), bfi.getHeight(), BufferedImage.TYPE_INT_RGB);
        int[][] ints = photoOps.bfiToInts(bfi);

        for (int i = 0; i < ints.length; i++) {
            for (int j = 0; j < ints[0].length; j++) {
                int pixel = ints[i][j];
                int[] rgb = photoOps.RGBToInts(pixel);
                int grey = (int) (0.299 * rgb[0] + 0.587f * rgb[1] + 0.114 * rgb[2]);

                int[] newRGB = new int[3];
                if (grey < 100) {  //暗部
                    for (int k = 0; k < newRGB.length; k++) {
//                        int newColor = rgb[k]*(index+50)/100; // 0.5-1.5 似乎有点大
                        int newColor = rgb[k] + rgb[k] * index / 250; // 0.5-1.5 似乎有点大

                        if (newColor > 255) {
                            newColor = 255;
                        }
                        newRGB[k] = newColor;
                    }
                    newBfi.setRGB(j, i, photoOps.intsToRGB(newRGB));
                } else if (grey < 130) {  //过渡区
                    for (int k = 0; k < newRGB.length; k++) {
                        int newColor = rgb[k] + rgb[k] * index / 400;
                        if (newColor > 255) {
                            newColor = 255;
                        }
                        newRGB[k] = newColor;
                    }
                    newBfi.setRGB(j, i, photoOps.intsToRGB(newRGB));
                } else {
                    newBfi.setRGB(j, i, pixel);
                }
            }
        }
        images.getAllSeriesBfi().add(newBfi);
        return photoOps.bfiToBytes(newBfi);
    }

    public BufferedImage getLastImage(int opsModel) {
        BufferedImage bfi;
        if (lastOpsModule == opsModel) {
            bfi = lastOpsImage;
        } else {
            lastOpsModule = opsModel;
            ArrayList<BufferedImage> allSeriesBfi = images.getAllSeriesBfi();
            bfi = allSeriesBfi.get(allSeriesBfi.size() - 1);
            lastOpsImage = bfi;
        }
        return bfi;
    }

    public Result Demo() {
        return Result.ok(images);
    }
}
