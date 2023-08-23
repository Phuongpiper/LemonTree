package com.example.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@Service
public class UploadServiceImpl implements UploadImagesService {

    @Autowired
    ServletContext app;

    public File save(MultipartFile file, String folder) {

        try {
            // Lấy đường dẫn tới thư mục 'src/main/resources/static/images/'
            String appRootPath = ResourceUtils.getURL("classpath:static/images/").getPath();
            File dir = new File(appRootPath);
            System.out.println(dir.getAbsolutePath());
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String s = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            String name = Integer.toHexString(s.hashCode()) + "-" + s.substring(s.lastIndexOf("."));
            File savedFile = new File(dir, name);
            file.transferTo(savedFile);
            System.out.println(savedFile.getAbsolutePath());

            return savedFile;
        } catch (IllegalStateException e) {
            // Handle IllegalStateException (e.g., if the file is larger than allowed)
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            // Handle FileNotFoundException (e.g., if the directory doesn't exist)
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            // Handle other IO exceptions
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

}
