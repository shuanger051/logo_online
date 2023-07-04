package com.qinghua.website.api.utils;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PdfBoxUtils {

    public final  static String  IMG_TYPE_JPG = "jpg";
    public final  static String  IMG_TYPE_PNG = "png";

    public static List<String> transfer2Img(File file, String savePath, String fileName,String type, int comBase) {
        PDDocument pdf = null;
        List<String> fileNames = new ArrayList<>();
        try {
            pdf = PDDocument.load(file);
            PDFRenderer pdfRenderer = new PDFRenderer(pdf);
            PDPageTree pageTree = pdf.getPages();
            int pageCounter = 0;
            for (PDPage page : pageTree) {
                BufferedImage bim = pdfRenderer.renderImage(pageCounter, 2F, ImageType.RGB);
                String fName = savePath + fileName + "_" + (pageCounter++) + "." + type;
                ImgUtils.saveMinImg(bim, fName,comBase,1D);
                fileNames.add(fName);
            }
            return fileNames;
        } catch (IOException e) {
            throw new BizException(SysConstant.ERROR_FILE_UPLOAD_FILE_10004);
        }finally {
            if(null != pdf){
                try {
                    pdf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
