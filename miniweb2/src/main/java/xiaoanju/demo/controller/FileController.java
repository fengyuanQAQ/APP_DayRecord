package xiaoanju.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xiaoanju.demo.service.FileService;

import javax.security.auth.Refreshable;
import javax.sound.midi.SoundbankResource;
import javax.websocket.server.PathParam;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理文件的上传和下载
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    //上传头像
    @PostMapping("/uploadHeadImage")
    public String uploadHeadImage(@RequestParam("file") MultipartFile file, @RequestParam("userid")
            String userid) {
        return fileService.uploadHeadFile(file,userid);
    }

    //获取头像字节流文件
    @GetMapping("/getHeadImage")
    public ResponseEntity<FileSystemResource> getHeadImage(@PathParam("userid")String userid){
        return fileService.getHeadFile(userid);
    }

    //上传分享图片
    @PostMapping("/uploadBgImage")
    public String uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("id")
            String id) {
        return fileService.uploadBgFile(file,id);
    }

    //获取图片字节流文件
    @GetMapping("/getBgImage")
    public ResponseEntity<FileSystemResource> getBgImage(@PathParam("id")String id){
        return fileService.getBgFile(id);
    }
}
