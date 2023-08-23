package com.example.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;



public interface UploadImagesService {
    
    File save(MultipartFile multipartFile, String folder);
}
