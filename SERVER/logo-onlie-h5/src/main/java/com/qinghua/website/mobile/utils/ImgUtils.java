package com.qinghua.website.mobile.utils;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;

@Slf4j
public class ImgUtils {

    private static final BASE64Decoder decoder = new BASE64Decoder();

    /**
     * 等比例压缩算法：
     * 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图
     *
     * @param srcURL  原图地址
     * @param comBase 压缩基数
     * @param scale   压缩限制(宽/高)比例  一般用1：
     *                当scale>=1,缩略图height=comBase,width按原图宽高比例;若scale<1,缩略图width=comBase,height按原图宽高比例
     * @throws Exception
     * @author shenbin
     * @createTime 2014-12-16
     * @lastModifyTime 2014-12-16
     */
    public static byte[] saveMinPhoto(String srcURL, double comBase,
                                      double scale) throws IOException {
        File srcFile = new File(srcURL);
        Image src = ImageIO.read(srcFile);
        return getBytes(src, comBase, scale);

    }


    /**
     * 保存到指定目录
     * @param image
     * @param savePath
     * @param comBase
     * @param scale
     * @throws IOException
     */
    public static void saveMinImg(Image image, String savePath, double comBase, double scale) throws IOException{
        BufferedImage tag = getBufferedImage(image,comBase,scale);
        File newFile = new File(savePath);
        if(!newFile.getParentFile().exists()){
            newFile.getParentFile().mkdirs();
        }
        FileOutputStream deskImage = new FileOutputStream(newFile); //输出到文件流
        try {
            ImageIO.write(tag, "jpeg", deskImage);
            deskImage.flush();
        } finally {
            try {
                if(null != deskImage) {
                    deskImage.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static BufferedImage getBufferedImage(Image src,double comBase, double scale){
        if(null == src){
            return null;
        }
        int srcHeight = src.getHeight(null);
        int srcWidth = src.getWidth(null);
        int deskHeight = 0;// 缩略图高
        int deskWidth = 0;// 缩略图宽
        double srcScale = (double) srcHeight / srcWidth;
        /**缩略图宽高算法*/
        if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
            if (srcScale >= scale || 1 / srcScale > scale) {
                if (srcScale >= scale) {
                    deskHeight = (int) comBase;
                    deskWidth = srcWidth * deskHeight / srcHeight;
                } else {
                    deskWidth = (int) comBase;
                    deskHeight = srcHeight * deskWidth / srcWidth;
                }
            } else {
                if ((double) srcHeight > comBase) {
                    deskHeight = (int) comBase;
                    deskWidth = srcWidth * deskHeight / srcHeight;
                } else {
                    deskWidth = (int) comBase;
                    deskHeight = srcHeight * deskWidth / srcWidth;
                }
            }
        } else {
            deskHeight = srcHeight;
            deskWidth = srcWidth;
        }
        ByteArrayOutputStream swapStream = null;
        BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);
        tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); //绘制缩小后的图
        return tag;
    }

    private static byte[] getBytes(Image src, double comBase, double scale) throws IOException {
        BufferedImage tag = getBufferedImage(src,comBase,scale);
        ByteArrayOutputStream swapStream = null;
        try {
            swapStream = new ByteArrayOutputStream();
            ImageIO.write(tag, "jpeg", swapStream);
            swapStream.flush();
            tag.flush();
            tag = null;
            return swapStream.toByteArray();
        } finally {
            if (null != swapStream) {
                try {
                    swapStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] base64StringToImage(String base64String, double comBase, double scale) throws IOException {
        byte[] bytes1 = decoder.decodeBuffer(base64String);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
        Image bi1 = (Image) ImageIO.read(bais);
        return getBytes(bi1, comBase, scale);
    }

    /**
     * 压缩本地图片保存到本地
     *
     * @param savePath      保存文件的路径，精确到最后一个文件夹
     * @param localPhotoUrl 本地图片路径，精确到文件
     * @param fileName      新的文件名
     * @return
     */
    public static String localImageCompress(String savePath, String localPhotoUrl, String fileName) {
        try {
            File f = new File(localPhotoUrl);
            long sourceImgValue = f.length() / 1024;
            BufferedImage sourceImg = ImageIO.read(new FileInputStream(f));
            int width = sourceImg.getWidth();//原图宽度
            int height = sourceImg.getHeight();//原图高度
            int hightValue = width;//宽度和高度的最大值
            if (height > width) hightValue = height;

            double scale111 = 0.5;
            if (1000 <= hightValue && hightValue < 2000) scale111 = 0.3;
            if (2000 <= hightValue && hightValue < 3000) scale111 = 0.2;
            if (3000 <= hightValue && hightValue < 4000) scale111 = 0.1;
            if (4000 <= hightValue) scale111 = BigDecimal.valueOf(500L).divide(BigDecimal.valueOf(hightValue), 2, BigDecimal.ROUND_UP).doubleValue();
            log.info("最终scale为：" + scale111);

            /**
             * 图片小于5M，压缩图片最大尺寸为512K
             * 大于5M，小于10M，压缩图片最大尺寸为1M
             * 大于10M，压缩图片最大尺寸为2M
             */
            int maxSize = 512;//压缩图片的最大尺寸
            if (sourceImgValue < 5 * 1024) maxSize = 512;
            if (5 * 1024 <= sourceImgValue && sourceImgValue < 10 * 1024) maxSize = 1024;
            if (10 * 1024 <= sourceImgValue) maxSize = 2 * 1024;

            String thumbnailPath = savePath + File.separator + fileName;
            Thumbnails.of(localPhotoUrl).scale(scale111).outputQuality(0.9).toFile(thumbnailPath);

            File f1 = new File(thumbnailPath);
            long returnValue = f1.length() / 1024;
            log.info("第一次生成图片缩略图，大小为：" + returnValue + "KB");
            // 如果图片大小超过1M，则继续压缩，直到图片大小小于或等于149KB
            if (returnValue > maxSize) {
                for (int i = 0; i < 4; i++) {
                    double scale = (i + 1) * 0.1;
                    double scaleQuality = 0.9 - scale * 2;
                    if (scale111 > scale) scale111 = scale111 - scale;
                    log.info("最终scale为：" + scale111);
                    log.info("最终scaleQuality为：" + scaleQuality);
                    Thumbnails.of(localPhotoUrl).scale(scale111).outputQuality(scaleQuality).toFile(thumbnailPath);
                    File f2 = new File(thumbnailPath);
                    returnValue = f2.length() / 1024;
                    log.info("继续压缩图片大小：" + returnValue + "KB");
                    if (returnValue > maxSize) {
                        continue;
                    } else {
                        break;
                    }
                }
            }
            return thumbnailPath;
        } catch (IOException e) {
            throw new BizException("图片压缩时出错", SysConstant.SYSTEM_ERROR_500.getCode());
        }
    }

    /**
     * 压缩base64编码至40K以内
     * @param base64Img
     * @return
     */
    public static String resizeImageTo40K(String base64Img) {
        try{
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(base64Img);
            InputStream   stream = new ByteArrayInputStream(bytes1);
            BufferedImage src = ImageIO.read(stream);
            BufferedImage output = Thumbnails.of(src).size(src.getWidth() / 3, src.getHeight() / 3).asBufferedImage();
            String base64 = imageToBase64(output);
            if (base64.length() - base64.length() / 8 * 2 > 40000) {
                output = Thumbnails.of(output).scale(1 / (base64.length() / 40000)).asBufferedImage();
                base64 = imageToBase64(output);
            }
            return base64;
        } catch (Exception e) {
            throw new BizException("压缩Base64编码时出错",SysConstant.SYSTEM_ERROR_500.getCode());
        }
    }

    // BufferedImage转换成base64，在这里需要设置图片格式，如下是jpg格式图片：
    public static String imageToBase64(BufferedImage bufferedImage) {
        Base64 encoder = new Base64();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
        } catch (IOException e) {
            throw new BizException("图片转Base64时出错",SysConstant.SYSTEM_ERROR_500.getCode());
        }
        return new String(encoder.encode((baos.toByteArray())));
    }

}
