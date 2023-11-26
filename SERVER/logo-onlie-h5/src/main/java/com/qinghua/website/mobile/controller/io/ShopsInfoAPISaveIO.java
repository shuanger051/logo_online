package com.qinghua.website.mobile.controller.io;

import com.qinghua.website.mobile.validation.DictValidator;
import com.qinghua.website.mobile.validation.IdCardValidator;
import com.qinghua.website.mobile.validation.NumValidator;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ShopsInfoAPISaveIO {

    @NotNull(message = "商户ID不能为空")
    private Long merchantId;

    /**
     * 商鋪名称
     */
    @NotNull(message = "商铺名称不能为空")
    @Size(max = 50,message = "商铺名称不能超过50个字符")
    private String shopName;

    /**
     * 店铺地址
     */
    @NotNull(message = "店铺地址不能为空")
    private String address;

    @NotNull(message = "详细地址不能为空")
    private String addressDetail;

    /**
     * 营业年限，1：3年以内,2：3-5年，3:5-10年，4:10年以上，5：百年老店
     */
    @DictValidator(value = "bizYears",message = "营业年限参数格式非法")
    private String bizYears;

    /**
     * 行业类型
     */
    @DictValidator(value = "industryType",message = "行业类型参数格式非法")
    private String industryType;

    /**
     * 店铺属性：1-自有，2-租住
     */
    @DictValidator(value = "shopsType",message = "店铺属性参数格式非法")
    private String shopsType;

    /**
     * 备注
     */
    @Size(max = 100,message = "备注信息不能超过100个字符")
    private String remark;

    /**
     * 经办人姓名
     */
    @NotNull(message = "经办人姓名不能为空")
    @Size(max = 10,message = "姓名最大不能超过10个字符")
    private String handledByName;

    /**
     * 经办人电话
     */
    @NotNull(message = "经办人电话不能为空")
    @Pattern(regexp = "^((0\\d{2,3}(-)?\\d{7,8})|(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8})$",message = "经办人电话格式错误")
    private String handledByPhone;

    /**
     * 经办人身份证号
     */
    @NotNull(message = "经办人身份证号不能为空")
    @IdCardValidator(message = "经办人身份证格式错误")
    private String handledByIdCard;

    /**
     * 经办人照片-正面
     */
    @NotNull(message = "经办人照片正面不能为空")
    private String handledByPhotoFront;

    /**
     * 经办人照片-反面
     */
    @NotNull(message = "经办人照片反面不能为空")
    private String handledByPhotoOpposite;

    /**
     * 宽度（米）
     */
    @NumValidator(message = "只允许最多两位小数的数字")
    @Max(value = 100,message = "宽度不能超过100")
    private String logoWidth;

    /**
     * 高度(米)
     */
    @NumValidator(message = "只允许两位小数的数字")
    @Max(value = 100,message = "高度不能超过100")
    private String logoHeight;

    /**
     * 材质：1-木质，2-石质，3-金属，4-其他
     */
    @DictValidator(value = "material",message = "材质参数格式非法")
    private String material;

    /**
     * 店招个数
     */
    private Long logoNum;

    /**
     * 店招个数
     */
    @Size(max = 50,message = "店招名称不能超过50个字符")
    private String logoName;

    /**
     * 店铺附件
     */
    private List<ShopsAttachmentIO> list;

    @Override
    public String toString() {
        return "ShopsInfoAPISaveIO{" +
                "merchantId=" + merchantId +
                ", address='" + address + '\'' +
                ", bizYears='" + bizYears + '\'' +
                ", industryType='" + industryType + '\'' +
                ", shopsType='" + shopsType + '\'' +
                ", list=" + list +
                '}';
    }

}
