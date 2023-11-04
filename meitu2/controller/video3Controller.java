package com.example.meitu2.controller;

import com.example.meitu2.utils.bfiOps;
import com.github.sarxos.webcam.Webcam;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.jitsi.service.neomedia.RawPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class video3Controller {
    /*
    工作总结：
    1. 现在情况：获取了BufferedImages，传入了FFmpeg,FFmpeg写入文件中
    2. 目标：将传入的FFmpeg获取输入流，读到RewPacket中
     */

    @Autowired
    Webcam webcam;

    @Autowired
    List<Webcam> webcams;
    @Autowired
    com.example.meitu2.pojos.websocket websocket;

    @Autowired
    Java2DFrameConverter converter;

    boolean isOpened = false;
    boolean isFileStreamOn = false;

    boolean needsLight = false;
    int lightIndex = 0;
    boolean needDuibidu = false;
    int duibiduIndex = 0;

    @GetMapping("openCam")
    public void openCam() throws IOException {
        if (!webcam.isOpen()) {
            webcam.open();
        }
        isOpened = true;
        DatagramSocket udpSocket = new DatagramSocket();
        InetAddress targetHost = InetAddress.getByName("localhost");
        int targetPort = 80;

        while (isOpened) {
            List<BufferedImage> list = new ArrayList<>();
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start <= 10000) {
                BufferedImage bfi = webcam.getImage();
                if (needsLight) {
                    bfi = bfiOps.light(bfi, lightIndex);
                }
                if (needDuibidu) {
                    bfi = bfiOps.duibidu(bfi, duibiduIndex);
                }
                list.add(bfi);
            }
            System.out.println("鹿丸！，开存！小节视频长度：" + (System.currentTimeMillis() - start));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] byteArray = outputStream.toByteArray();
            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputStream, webcam.getViewSize().width, webcam.getViewSize().height);
            recorder.setVideoCodecName("lib264");
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("mp4");
            recorder.setFrameRate(24);
            System.out.println("recorder: " + recorder);
            recorder.start();
            for (int i = 0; i < list.size(); i++) {
                Frame frame = converter.getFrame(list.get(i));
                recorder.record(frame);
            }
            recorder.stop();
            recorder.release();
            System.out.println("后台保存完毕");
            RawPacket rtpPacket = new RawPacket(byteArray, 0, byteArray.length);//构造中需要传入起始终结，可能表明不是让一遍传完
            DatagramPacket udpPacket = new DatagramPacket(rtpPacket.getBuffer(), rtpPacket.getLength(), targetHost, targetPort);
            udpSocket.send(udpPacket);

            udpSocket.close();
            websocket.sendOneMessage("0", "down");
        }
        webcam.close();
    }

    @GetMapping("closeCam")
    public void closeCam() {
        isOpened = false;
    }

    @GetMapping("video/light")
    public void light(Integer index) {
        needsLight = true;
        lightIndex = index;
    }

    @GetMapping("video/duibidu")
    public void duibidu(Integer index) {
        needDuibidu = true;
        duibiduIndex = index;
    }

    @GetMapping("RTPThread")
    public void RTPThread() throws SocketException, UnknownHostException, FFmpegFrameRecorder.Exception, InterruptedException {
        if (!webcam.isOpen()) {
            webcam.open();
        }
        isOpened = true;
        DatagramSocket udpSocket = new DatagramSocket();
        InetAddress targetHost = InetAddress.getByName("localhost");
        int targetPort = 2244;

//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        File file = new File("Demo.mp4");
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(file.getAbsoluteFile(), webcam.getViewSize().width, webcam.getViewSize().height);
        int a = 0;
        recorder.setVideoCodecName("lib264");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("mp4");
        recorder.setFrameRate(24);
        System.out.println("recorder: " + recorder);
        recorder.start();
        Object lock = new Object();

        //任务：一个线程负责录制视频，并将视频转入outputStream流，另一个线程读取流，建包，发送包

        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                while (isFileStreamOn) {
                    InputStream stream = new FileInputStream(file);
                    synchronized (lock) {
                        while (stream.available() > 0) {
                            byte[] bytes = new byte[1024];
                            int length = stream.read(bytes);
                            RawPacket rawPacket = new RawPacket(bytes, 0, length);
                            DatagramPacket udpPacket = new DatagramPacket(rawPacket.getBuffer(), rawPacket.getLength(), targetHost, targetPort);
                            udpSocket.send(udpPacket);

                            System.out.println("发送数据包 " + Arrays.toString(udpPacket.getData()));
                        }
                        System.out.println("一份file读完，更新file");
//                        file = new File(file.getAbsoluteFile());
                        try (FileWriter writer = new FileWriter(file)) {
                            writer.write(""); // 写入空字符串，清空文件内容
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        stream = new FileInputStream(file);
                    }
                }
                System.out.println("一号外层while结束");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        while (isOpened) {
            //问题：当输出流中缓存了太多时要重置，更新只能由存入端完成，那么要保证重置时不被访问
            synchronized (lock) {

                System.out.println("正在添加FFmpag");
                BufferedImage image = webcam.getImage();
                Frame frame = converter.getFrame(image);
                recorder.record(frame);

                if (!isFileStreamOn) {
                    isFileStreamOn = true;
                }
            }
            Thread.yield();

        }
        recorder.stop();
        recorder.release();
    }
    /*
    客户端的思路：
    线程A将视频帧数据转入FFmpag工具中，目的是用工具写入输出流中，线程B不断获取输出流的信息
    为了防止流数据过大，某个线程需要将流重置一遍 ———— 读线程重置 但需要确保读和重置之间不会被写入 | 加锁
    此处可以用队列优化吗：
        思路：写入流当写入一部分后，将数据打包装入队列中，读流按个读
        问题：由于用到别人的轮子，FFmpagFrameRecorder绑定一个流对象，不太好加到队列里


    模拟前端的服务端的思路：
       > 见内个项目

    关于两种将outputStream转储为package的方法
    1. 持久读，在锁内部持续读取流数据
    2. 立马读，读完立即释放锁，将读取的内容存储到超大byte[]中，再将数据切片读取

    1加锁的粒度较大，对outputStream的阻塞事件较长
    2占用的连续大空间比较多



    bug记录1：
    二号线程的notify无法唤醒一号线程的wait
    原因是：
    一号还没睡呢
     */
}
