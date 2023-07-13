package com.qinghua.website.server.utils;

import com.qinghua.website.server.constant.SysConstant;
import com.qinghua.website.server.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@Slf4j
public class FileUtils {

    /**
     * 删除指定文件夹及其内全部文件
     *
     * @param filePath
     */
    public static void deleteFolders(String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    Files.delete(file);
                    log.info("删除文件: {}", file);
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    log.info("文件夹被删除: {}", dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        try{
            File file = new File(filePath);
            if(file.delete()){
                log.info("成功删除文件：{}",filePath);
            }else{
                throw new BizException(SysConstant.ERROR_DELETE_FILE_FAIL);
            }
        }catch (Exception e){
            log.error("删除文件失败");
            throw new BizException(SysConstant.ERROR_DELETE_FILE_FAIL);
        }
    }

}
