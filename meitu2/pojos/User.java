package com.example.meitu2.pojos;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope(value = "prototype")
public class User {

    Integer userId;

    String userName;

    //在OSS中属于个人存储的位置
    String bucket;
}
