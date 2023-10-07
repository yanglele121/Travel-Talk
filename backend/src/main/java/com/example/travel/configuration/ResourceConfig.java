package com.example.travel.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    // 对静态资源进行重映射，从而确保实时访问到最新的图片资源
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String userProfilesPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\upload\\userAvatarUrl\\";
        String picturePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\upload\\pictures\\";
        String articleCoverPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\upload\\covers\\";
        String announcementUrl = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\upload\\announcement\\";
        String albumpic = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\upload\\album\\";
        String siteCoverPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\upload\\site/";

        // 用户头像图片映射
        registry.addResourceHandler("/upload/userAvatarUrl/**").addResourceLocations("file:" + userProfilesPath);

        // 封面映射
        registry.addResourceHandler("/upload/covers/**").addResourceLocations("file:" + articleCoverPath);

        // 全站其他图片映射
        registry.addResourceHandler("/upload/pictures/**").addResourceLocations("file:" + picturePath);

        // 资讯图映射
        registry.addResourceHandler("/upload/announcement/**").addResourceLocations("file:" + announcementUrl);

        // 圈子图映射
        registry.addResourceHandler("/upload/album/**").addResourceLocations("file:" + albumpic);

        // 景点图片映射
        registry.addResourceHandler("/upload/site/**").addResourceLocations("file:" + siteCoverPath);
    }
}
