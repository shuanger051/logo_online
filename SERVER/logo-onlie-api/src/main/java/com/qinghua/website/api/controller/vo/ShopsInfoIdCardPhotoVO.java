package com.qinghua.website.api.controller.vo;

import lombok.Data;

@Data
public class ShopsInfoIdCardPhotoVO {

    private Long id;

    /**
     * 身份证正面照
     */
    private String handledByPhotoFront;

    /**
     * 身份证反面照
     */
    private String handledByPhotoOpposite;

    @Override
    public String toString() {
        return "ShopsInfoIdCardPhotoVO{" +
                "id=" + id +
                ", handledByPhotoFront='" + handledByPhotoFront + '\'' +
                ", handledByPhotoOpposite='" + handledByPhotoOpposite + '\'' +
                '}';
    }

}
