package com.afridi.gamingbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadRequest {

    private String id;
    private String name;
    private String imageUrl;
    private String webUrl;
    private Long imageSize;
}
