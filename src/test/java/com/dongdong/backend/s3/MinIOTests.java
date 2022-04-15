package com.dongdong.backend.s3;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinIOTests {

    @Test
    void minioClientTest() throws InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, InvalidPortException, InvalidEndpointException, RegionConflictException {
        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        MinioClient minioClient = new MinioClient("http://172.19.240.21:30900", "minio", "minio123");

        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists("dongdong");
        if (isExist) {
            System.out.println("Bucket already exists.");
        } else {
            // 创建一个名为dongdong的存储桶，用于存储照片文件。
            minioClient.makeBucket("dongdong");
        }

        var picPath = "src/test/resources/mypic.jpeg";
        var fis = new FileInputStream(picPath);
        // 使用putObject上传一个文件到存储桶中。
        minioClient.putObject("dongdong", "mypic", fis, new PutObjectOptions(fis.available(), PutObjectOptions.MAX_PART_SIZE));
        System.out.println(picPath + " is successfully uploaded as mypic.jpeg to `dongdong` bucket.");

        var is = minioClient.getObject("dongdong", "mypic");
        var picOutPath = "src/test/resources/mypic-out.jpeg";
        var bytes = is.readAllBytes();
        Files.write(Path.of(picOutPath), bytes);
        System.out.println("mypic.jpeg is successfully downloaded and saved as mypic-out.jpeg to " + picOutPath + ".");
    }

}
