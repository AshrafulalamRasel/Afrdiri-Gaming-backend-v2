package com.afridi.gamingbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageUrlResponse {

    private String fileName;
    private String fileId;
    private String webUrl;
    private String imageUrl;
    private byte[] imageSize;


}
