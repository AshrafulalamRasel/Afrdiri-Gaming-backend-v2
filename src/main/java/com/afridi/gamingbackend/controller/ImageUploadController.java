package com.afridi.gamingbackend.controller;

import com.afridi.gamingbackend.dto.response.IdentityResponse;
import com.afridi.gamingbackend.dto.response.ImageUrlResponse;
import com.afridi.gamingbackend.services.ImageUploadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ImageUploadController {

    private ImageUploadService imageUploadService;

    @PostMapping("/upload/image")
    public ResponseEntity<IdentityResponse> insertImage(@RequestParam("file") MultipartFile file, String webUrl) throws IOException {

        return new ResponseEntity(imageUploadService.creatateImage(file,webUrl), HttpStatus.OK);
    }

    @RequestMapping(value = "/getFile/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getFile(@PathVariable String id){
      return imageUploadService.getImageByFileName(id);
    }

    @GetMapping("/getUrl")
    public List<ImageUrlResponse> getUrl(){
        return imageUploadService.getUrlResponse();
    }
}
