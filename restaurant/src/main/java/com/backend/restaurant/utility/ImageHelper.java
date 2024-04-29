package com.backend.restaurant.utility;

import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
public class ImageHelper {
    public boolean uploadImage(String path, MultipartFile file, String newFileName){

        //fullpath
        String filePath = path + File.separator + newFileName;

        //create folder if not created
        File location = new File(path);

        if(!location.exists()){
            location.mkdir();
        }
        //file copy
        try{
            Files.copy(file.getInputStream(), Paths.get(filePath));
        }
        catch (IOException e){
            return false;
        }

        return true;
    }

    InputStream getImages(String fileName){
        InputStream is = null;
        try{
            is = new FileInputStream(fileName);
        }
        catch (FileNotFoundException e){
            return null;
        }

        return is;


    }

    public boolean  deleteImage(String path, String imageUrl){
        Path imagePath = Paths.get(path + File.separator + imageUrl);

        try{
            Files.delete(imagePath);
        }catch (IOException e){
            return false;
        }
        return true;
    }
}
