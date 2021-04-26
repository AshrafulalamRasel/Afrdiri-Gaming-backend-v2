package com.afridi.gamingbackend.services;


import com.afridi.gamingbackend.domain.model.ImageUploadHeader;
import com.afridi.gamingbackend.domain.repository.ImageUploadRepository;
import com.afridi.gamingbackend.dto.request.ImageUploadRequest;
import com.afridi.gamingbackend.dto.response.ImageUrlResponse;
import com.afridi.gamingbackend.dto.response.IdentityResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ImageUploadService {



    private final ImageUploadRepository imageUploadRepository;

    private  final  ServletContext context;


    public IdentityResponse createImage(MultipartFile inputFile,String webUrl) {

        UUID id = UUID.randomUUID();
        String uuid = id.toString();

        ImageUploadRequest imageUploadRequest = new ImageUploadRequest();
        if (!inputFile.isEmpty()) {

            try {

                String originalFilename = inputFile.getOriginalFilename();

                File folder=new File("D:/AfridiGameing/Afrdiri-Gaming-backend-v2/src/main/resources/image-upload");

                if (!folder.exists()) {

                    if (folder.mkdir()) {

                        String destinationUrl = folder+ File.separator + originalFilename;
                        File destinationFile = new File(destinationUrl);
                        inputFile.transferTo(destinationFile);
                        imageUploadRequest.setId(uuid);
                        imageUploadRequest.setName(originalFilename);
                        imageUploadRequest.setWebUrl(webUrl);
                        imageUploadRequest.setImageUrl(destinationUrl);
                        imageUploadRequest.setImageSize(inputFile.getSize());
                        System.out.println(destinationFile);
                    }

                    else {

                        System.out.println("Failed to create directory!");
                    }
                }

            }

            catch (Exception e) {
                System.out.println("---" + e.getMessage());
            }

        }

        else {
            System.out.println("URL not found");
        }


        ImageUploadHeader imageUploadHeader = new ImageUploadHeader();

        imageUploadHeader.setId(imageUploadRequest.getId());
        imageUploadHeader.setName(imageUploadRequest.getName());
        imageUploadHeader.setWebUrl(imageUploadRequest.getWebUrl());
        imageUploadHeader.setImageUrl(imageUploadRequest.getImageUrl());
        imageUploadHeader.setImageSize(imageUploadRequest.getImageSize());

        imageUploadRepository.saveAndFlush(imageUploadHeader);

        return new IdentityResponse(uuid);
    }


    public ImageUrlResponse getUrlResponse (String id){

        Optional<ImageUploadHeader> imageUploadHeaderOptional = imageUploadRepository.findAllById(id);

        ImageUploadHeader imageUploadHeader = imageUploadHeaderOptional.get();

        ImageUrlResponse imageUrlResponse = new ImageUrlResponse();

        imageUrlResponse.setFileName(imageUploadHeader.getName());
        imageUrlResponse.setWebUrl(imageUploadHeader.getWebUrl());


        return imageUrlResponse;
    }

    public ResponseEntity<byte[]> getImageByFileName (String id)     {

        ImageUrlResponse imageUrlResponse = null;

        try {

            Optional<ImageUploadHeader> imageUploadHeaderOptional = imageUploadRepository.findAllById(id);

            ImageUploadHeader imageUploadHeader = imageUploadHeaderOptional.get();



            imageUrlResponse = new ImageUrlResponse();

            int nRead;
            byte[] data = new byte[1024];
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            String destinationUrl = "D:/AfridiGameing/Afrdiri-Gaming-backend-v2/src/main/resources/image-upload";
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
}
