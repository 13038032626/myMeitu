package com.example.meitu2.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.*;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class OSSUtils {

    String endPoint = "https://oss-cn-hangzhou.aliyuncs.com";
    String accessId = "LTAI5tQ9YSi1oGfP9ERREzoY";
    String accessKey = "ohHPZS24lBmYZx6wZB3DIIZycmgIkX";

    OSS ossClient = new OSSClientBuilder().build(endPoint,accessId,accessKey);

    public void createBucket(String bucketName){
        if(!ossClient.doesBucketExist(bucketName)){
            ossClient.createBucket(bucketName);
            CreateBucketRequest request = new CreateBucketRequest(bucketName);
            request.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(request);
        }else {
            System.out.println("bucket重名");
        }
    }
    public void uploadFile(String bucketName, InputStream inputStream){
        ossClient.putObject(new PutObjectRequest(bucketName,"key",inputStream));
    }
    public ArrayList<BufferedImage> getAllPhotos(String bucketName) throws IOException {

        ArrayList<BufferedImage> photos = new ArrayList<>();

        ObjectListing objectListing = ossClient.listObjects(bucketName);
        List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for (OSSObjectSummary summary:objectSummaries
             ) {
            String key = summary.getKey();
            OSSObject object = ossClient.getObject(bucketName, key);
            InputStream objectContent = object.getObjectContent();
            BufferedImage bufferedImage = photoOps.inputStreamToBfi(objectContent);
            photos.add(bufferedImage);
        }
        return photos;
    }
}
