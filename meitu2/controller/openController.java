package com.example.meitu2.controller;

import com.example.meitu2.pojos.images;
import com.example.meitu2.utils.Result;
import com.example.meitu2.utils.innerCompressionUtils;
import com.example.meitu2.utils.photoOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

@RestController
@CrossOrigin
public class openController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    images images;

    @PostMapping("picture/open")
    public Result upload(MultipartFile file) throws IOException {
        System.out.println("到达！");
        String fileName = file.getOriginalFilename();  //获取文件原名
        BufferedImage bfi = ImageIO.read(file.getInputStream());
        System.out.println(fileName);
//        System.out.println(bfi);


        byte[] bytes1 = innerCompressionUtils.compressToOneChannel(bfi);
//        System.out.println(Arrays.toString(bytes1));
        File file1 = new File("写入测试压缩版本.dat");
        OutputStream outputStream1 = new FileOutputStream(file1);
        outputStream1.write(bytes1);
        File file2 = new File("写入测试未压缩版本.dat");
        OutputStream outputStream2 = new FileOutputStream(file2);
        outputStream2.write(photoOps.bfiToBytes(bfi));

        outputStream1.close();
        outputStream2.close();
        System.out.println("写入结束");


        images.setBfi(bfi);
        images.getAllSeriesBfi().add(bfi);
        images.setWidth(bfi.getWidth());
        images.setHeight(bfi.getHeight());
        images.setFileName(fileName);
        System.out.println("bean对象bfi："+bfi.toString());
        //画入静态文件中
        byte[] bytes = photoOps.bfiToBytes(bfi);
        File filele = new File("C:\\Users\\吴松林\\IdeaProjects\\myMeitu\\src\\main\\resources\\static\\Demo2.png");
        OutputStream outputStream = new FileOutputStream(filele);
        outputStream.write(bytes);
        return Result.ok("你好");
    }
    @GetMapping("picture/image")
    public byte[] getImage() throws IOException {
        BufferedImage bfi = images.getBfi();
        byte[] bytes = photoOps.bfiToBytes(bfi);
        return bytes;
    }



    public String bfiToData(BufferedImage bfi){
        int width = bfi.getWidth();
        int height = bfi.getHeight();
        int[] data = new int[width*height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = bfi.getRGB(j, i);
                data[i*width+j] = rgb;
            }
        }
        return Arrays.toString(data);
    }
}

