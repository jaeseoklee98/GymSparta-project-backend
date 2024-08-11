package com.sparta.fitpleprojectbackend.s3imgupload.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class S3Service {

  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  private String dir = "upload";

  public String uploadFile(MultipartFile file) throws IOException {
    String fileName = generateFileName(file);
    String key = dir + "/" + fileName; // 디렉토리 경로와 파일 이름을 결합하여 객체 키 생성

    amazonS3.putObject(bucketName, key, file.getInputStream(), getObjectMetadata(file));

    // 업로드한 객체의 URL을 동적으로 생성
    URL fileUrl = amazonS3.getUrl(bucketName, key);
    return fileUrl.toString();
  }

  private ObjectMetadata getObjectMetadata(MultipartFile file) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(file.getContentType());
    objectMetadata.setContentLength(file.getSize());
    return objectMetadata;
  }

  private String generateFileName(MultipartFile file) {
    return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
  }
}