package com.sparta.fitpleprojectbackend.s3imgupload.controller;

import com.sparta.fitpleprojectbackend.s3imgupload.service.S3Service;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class S3Controller {

  private final S3Service sService;

  @PostMapping
  public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
    String url = sService.uploadFile(file);
    return ResponseEntity.ok(url);
  }
}