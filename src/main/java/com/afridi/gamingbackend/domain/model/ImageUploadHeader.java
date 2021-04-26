package com.afridi.gamingbackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "IMAGEUPLOAD")
public class ImageUploadHeader extends BaseEntity {

    private String id;
    private String name;
    private String imageUrl;
    private String webUrl;
    private Long imageSize;


}
