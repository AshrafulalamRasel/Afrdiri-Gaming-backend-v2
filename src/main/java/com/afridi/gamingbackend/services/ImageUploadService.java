package com.afridi.gamingbackend.services;


import com.afridi.gamingbackend.domain.model.ImageUploadHeader;
import com.afridi.gamingbackend.domain.repository.ImageUploadRepository;
import com.afridi.gamingbackend.dto.request.ImageUploadRequest;
import com.afridi.gamingbackend.dto.response.ImageUrlResponse;
import com.afridi.gamingbackend.dto.response.IdentityResponse;
import com.afridi.gamingbackend.util.FileUploadUtil;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory;

@AllArgsConstructor
@Service
public class ImageUploadService {



    private final ImageUploadRepository imageUploadRepository;

    private  final  ServletContext context;
    private final String url = "http://143.198.196.23:8088/api/" ;


    public IdentityResponse creatateImage(MultipartFile inputFile, String webUrl) throws IOException {

        UUID id = UUID.randomUUID();
        String uuid = id.toString();

        ImageUploadHeader imageUploadRequest = new ImageUploadHeader();

        if (imageUploadRepository.count()<5) {

        String fileName = StringUtils.cleanPath(inputFile.getOriginalFilename());
        imageUploadRequest.setName(fileName);
        imageUploadRequest.setImageSize(inputFile.getSize());
        imageUploadRequest.setWebUrl(webUrl);
        imageUploadRequest.setId(uuid);

        String uploadDir = "userPhoto/" + imageUploadRequest.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, inputFile);
        String destinationUrl = uploadDir+ File.separator + fileName;
        imageUploadRequest.setImageUrl(destinationUrl);

            imageUploadRepository.saveAndFlush(imageUploadRequest);
        }
        else {
            throw  new  RuntimeException("Please Delete one Image");
        }
        return new IdentityResponse(uuid);

    }


    public List<ImageUrlResponse> getUrlResponse (){

        List<ImageUploadHeader> imageUploadHeaderList = imageUploadRepository.findAll();
        List<ImageUrlResponse> imageUrlResponseList = new ArrayList<>();

        for (ImageUploadHeader imageUploadHeader: imageUploadHeaderList){



                System.out.println("imageUploadRepository.count()"+imageUploadRepository.count());

                ImageUrlResponse imageUrlResponse = new ImageUrlResponse();

                imageUrlResponse.setFileName(imageUploadHeader.getName());
                imageUrlResponse.setFileId(imageUploadHeader.getId());
                imageUrlResponse.setWebUrl(imageUploadHeader.getWebUrl());
                imageUrlResponse.setImageUrl(url + "auth/getFile/" + imageUploadHeader.getId());
                imageUrlResponseList.add(imageUrlResponse);

        }


        return imageUrlResponseList;
    }

    public ResponseEntity<byte[]> getImageByFileName (String id)     {

        ImageUrlResponse imageUrlResponse = null;

        try {

            Optional<ImageUploadHeader> imageUploadHeaderOptional = imageUploadRepository.findAllById(id);

            ImageUploadHeader imageUploadHeader = imageUploadHeaderOptional.get();
            String dirName = "userPhoto";

            Path uploadDir = Paths.get(dirName);
            String uploadPath = uploadDir.toFile().getAbsolutePath();

            imageUrlResponse = new ImageUrlResponse();

            int nRead;
            byte[] data = new byte[1024];
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            String destinationUrl = uploadPath+"/"+id;
            File destinationFile = new File(destinationUrl+ File.separator + imageUploadHeader.getName());
            InputStream inputStream = new FileInputStream(destinationFile);
            InputStream is = inputStream;

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            imageUrlResponse.setImageSize(buffer.toByteArray());
            imageUrlResponse.setWebUrl(imageUploadHeader.getWebUrl());
            System.out.println("---" + imageUrlResponse.getWebUrl());

        } catch (Exception e) {
            System.out.println("---" + e.getMessage());

        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageUrlResponse.getImageSize());
    }


    public void deleteImage(String id) throws IOException {

        String dirName = "userPhoto";
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        String destinationUrl = uploadPath+"/"+id;

        Optional<ImageUploadHeader> imageUploadHeaderOptional = imageUploadRepository.findAllById(id);
        ImageUploadHeader imageUploadHeader = imageUploadHeaderOptional.get();
        File folder = new File(destinationUrl);

            FileUtils.forceDelete(folder);
            imageUploadRepository.delete(imageUploadHeader);


    }


}
