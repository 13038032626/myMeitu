package com.example.meitu2.config;

import com.github.sarxos.webcam.Webcam;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Configuration
public class videoConfig {

    @Bean
    public OpenCVFrameGrabber getGrabber() {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        return grabber;
    }

    @Bean
    public OpenCVFrameConverter.ToIplImage getOpenCVConverter() {
        OpenCVFrameConverter.ToIplImage openCVConverter = new OpenCVFrameConverter.ToIplImage();
        return openCVConverter;
    }

    ;
    @Bean
    public Webcam getCam() {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));
        return webcam;
    }

    @Bean
    public FFmpegFrameRecorder getRecorder(Webcam webcam){
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("src/main/resources/static/output.avi",webcam.getViewSize().width,webcam.getViewSize().height);
        recorder.setVideoCodecName("lib264");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
        recorder.setFormat("avi");
        recorder.setFrameRate(24);
        return recorder;
    }

    @Bean
    public Java2DFrameConverter getConverter(){
        return new Java2DFrameConverter();
    }

}
