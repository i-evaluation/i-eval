package com.litevar.ieval.llm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @Author  action
 * @Date  2025/11/10 10:08
 * @company litevar
 **/
public class ImageUtil {
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
    public static String toBase64(String picturePath) {
        try {
            // 读取文件为字节数组
            byte[] imageBytes = Files.readAllBytes(Paths.get(picturePath));

            // 将字节数组编码为Base64字符串
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            if(picturePath.endsWith(".png")) {
                return "data:image/png;base64," + base64Image;
            }
            else if(picturePath.endsWith(".jpeg")) {
                return "data:image/jpeg;base64," + base64Image;
            }
            else if(picturePath.endsWith(".jpg")) {
                return "data:image/jpg;base64," + base64Image;
            }
            return base64Image;
        } catch (Exception e) {
            logger.error("Error occurred", e);
        }
        return null;
    }
}
