package com.example.meitu2;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Demo {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        Mat a = Mat.eye(3,3, CvType.CV_8UC1);
        System.out.println(a.dump());
    }
}
