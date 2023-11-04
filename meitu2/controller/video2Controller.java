package com.example.meitu2.controller;

import com.example.meitu2.pojos.websocket;
import com.example.meitu2.utils.Result;
import com.example.meitu2.utils.photoOps;
import com.github.sarxos.webcam.Webcam;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class video2Controller {

    @Autowired
    Webcam webcam;

    @Autowired
    List<Webcam> webcams;
    @Autowired
    websocket websocket;

//    @Autowired
//    FFmpegFrameRecorder recorder;

    @Autowired
    Java2DFrameConverter converter;

    int num = 0;

    @GetMapping("/openWebCam")
    public byte[] open() throws IOException {
        webcam.open();
        BufferedImage image = webcam.getImage();
        System.out.println("width  "+image.getWidth());
        System.out.println("height  "+image.getHeight());
        return photoOps.bfiToBytes(image);
    }

//    @GetMapping("/setVideo")
//    public synchronized Result setVideo() throws FFmpegFrameRecorder.Exception {  //默认获取三十秒的视频
//        //加锁是为了防止webcam被多个线程调用
//        if(!webcam.isOpen()){
//            webcam.open();
//        }
//        num++;
//        List<BufferedImage> list = new ArrayList<>();
//        long start = System.currentTimeMillis();
//        while (System.currentTimeMillis()-start<=10000){
//            BufferedImage bfi = webcam.getImage();
//            list.add(bfi);
//        }
//        System.out.println("鹿丸！，开存！");
//        webcam.close();
//        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("src/main/resources/static/output.mp4",webcam.getViewSize().width,webcam.getViewSize().height);
//        recorder.setVideoCodecName("lib264");
//        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
//        recorder.setFormat("mp4");
//        recorder.setFrameRate(24);
//        System.out.println("recorder: "+recorder);
//        recorder.start();
//        for (int i = 0; i < list.size(); i++) {
//            Frame frame = converter.getFrame(list.get(i));
//            recorder.record(frame);
//        }
//        recorder.stop();
//        recorder.release();
//        System.out.println("存完");
//        return Result.ok("down");
//    }


}
