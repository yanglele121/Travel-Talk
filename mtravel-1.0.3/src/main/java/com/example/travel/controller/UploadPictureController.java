package com.example.travel.controller;

import com.example.travel.vo.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class UploadPictureController {

    // 处理上传图片
    @RequestMapping(value="/uploadPicture")
    public Result uploadPicture(@RequestParam(value = "file") MultipartFile file,
                                HttpServletRequest request) throws IOException {
        String resourcePath = "upload/pictures";
        String dirPath = new String("src\\main\\resources\\static\\" + resourcePath);

        File fileDirPath = new File(dirPath);
        if(!fileDirPath.exists()) {
            fileDirPath.mkdirs();
        }

        String fileName = UUID.randomUUID().toString();
        File savePath = new File(fileDirPath.getAbsolutePath() + File.separator + fileName + ".jpg");
        file.transferTo(savePath);

        String imgURL = "http://localhost:8080/" + resourcePath + "/" + fileName + ".jpg";

        return Result.success(imgURL);
    }

    // 相片集
    @RequestMapping(value="/uploadPicAlbum")
    public ModelAndView uploadPicAlbum(@RequestParam(value = "file") MultipartFile file,
                                       HttpServletRequest request) throws IOException {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        String resourcePath = "upload/picAlbum";
        String dirPath = new String("src\\main\\resources\\static\\" + resourcePath);

        File fileDirPath = new File(dirPath);
        if(!fileDirPath.exists()) {
            fileDirPath.mkdirs();
        }

        String fileName = UUID.randomUUID().toString();
        File savePath = new File(fileDirPath.getAbsolutePath() + File.separator + fileName + ".jpg");
        file.transferTo(savePath);

        String imgURL = "http://localhost:8080/" + resourcePath + "/" + fileName + ".jpg";

        modelAndView.addObject("imgURL", imgURL);
        modelAndView.addObject("status", "success");

        return modelAndView;
    }

    // 文章封面
    @RequestMapping(value="/uploadCover")
    public Result uploadCover(@RequestParam(value = "file") MultipartFile file,
                              HttpServletRequest request) throws IOException {
        String resourcePath = "upload/covers";
        String dirPath = new String("src\\main\\resources\\static\\" + resourcePath);

        File fileDirPath = new File(dirPath);
        if(!fileDirPath.exists()) {
            fileDirPath.mkdirs();
        }

        String fileName = UUID.randomUUID().toString();
        File savePath = new File(fileDirPath.getAbsolutePath() + File.separator + fileName + ".jpg");
        file.transferTo(savePath);

        String imgURL = "http://localhost:8080/" + resourcePath + "/" + fileName + ".jpg";

        return Result.success(imgURL);
    }

    @RequestMapping(value="/uploadAnnouncementCover")
    public Result uploadAnnouncementCover(@RequestParam(value = "file") MultipartFile file,
                                          HttpServletRequest request) throws IOException {
        String resourcePath = "upload/announcement";
        String dirPath = new String("src\\main\\resources\\static\\" + resourcePath);

        File fileDirPath = new File(dirPath);
        if(!fileDirPath.exists()) {
            fileDirPath.mkdirs();
        }

        String fileName = UUID.randomUUID().toString();
        File savePath = new File(fileDirPath.getAbsolutePath() + File.separator + fileName + ".jpg");
        file.transferTo(savePath);

        String imgURL = "http://localhost:8080/" + resourcePath + "/" + fileName + ".jpg";

        return Result.success(imgURL);
    }

    @RequestMapping(value="/uploadAlbumPic")
    public Result uploadAlbumPic(@RequestParam(value = "file") MultipartFile file,
                                 HttpServletRequest request) throws IOException {
        String resourcePath = "upload/album";
        String dirPath = new String("src\\main\\resources\\static\\" + resourcePath);

        File fileDirPath = new File(dirPath);
        if(!fileDirPath.exists()) {
            fileDirPath.mkdirs();
        }

        String fileName = UUID.randomUUID().toString();
        File savePath = new File(fileDirPath.getAbsolutePath() + File.separator + fileName + ".jpg");
        file.transferTo(savePath);

        String imgURL = "http://localhost:8080/" + resourcePath + "/" + fileName + ".jpg";

        return Result.success(imgURL);
    }

    @RequestMapping(value="/uploadSite")
    public Result uploadSite(@RequestParam(value = "file") MultipartFile file,
                             HttpServletRequest request) throws IOException {
        String resourcePath = "upload/site";
        String dirPath = new String("src\\main\\resources\\static\\" + resourcePath);

        File fileDirPath = new File(dirPath);
        if(!fileDirPath.exists()) {
            fileDirPath.mkdirs();
        }

        String fileName = UUID.randomUUID().toString();
        File savePath = new File(fileDirPath.getAbsolutePath() + File.separator + fileName + ".jpg");
        file.transferTo(savePath);

        String imgURL = "http://localhost:8080/" + resourcePath + "/" + fileName + ".jpg";

        return Result.success(imgURL);
    }

    @RequestMapping(value = "/uploadUserProfile")
    public Result uploadUserProfile(@RequestParam(value = "file") MultipartFile file,
                                    HttpServletRequest request) throws IOException {
        String resourcePath = "upload/userAvatarUrl";
        String dirPath = new String("src\\main\\resources\\static\\" + resourcePath);
        File fileDirPath = new File(dirPath);
        if (!fileDirPath.exists()) {
            fileDirPath.mkdirs();
        }

        String fileName = UUID.randomUUID().toString();
        File savePath = new File(fileDirPath.getAbsolutePath() + File.separator + fileName + ".jpg");
        file.transferTo(savePath);

        String imgURL = "http://localhost:8080/" + resourcePath + "/" + fileName + ".jpg";

        return Result.success(imgURL);
    }
}
