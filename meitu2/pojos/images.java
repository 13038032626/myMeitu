package com.example.meitu2.pojos;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

@Component
@Data
public class images {

    BufferedImage bfi;

    Integer width;

    Integer height;

    String fileName;

    ArrayList<BufferedImage> allSeriesBfi = new ArrayList<>();
}
