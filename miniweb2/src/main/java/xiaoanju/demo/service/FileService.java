package xiaoanju.demo.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    public String uploadHeadFile(MultipartFile file,String userid) {
        String currentPath = System.getProperty("user.dir");
        String imageDir = File.separator + currentPath + File.separator + "file" + File.separator + "headImage"
                +File.separator;
        String newFileName = userid + ".jpg";
        return commomUpload(file,imageDir+newFileName);
    }

    public String uploadBgFile(MultipartFile file, String sharingid){
        String currentPath = System.getProperty("user.dir");
        String imageDir = File.separator + currentPath + File.separator + "file" + File.separator + "backImage"
                +File.separator;
        String newFileName =sharingid + ".jpg";
        return commomUpload(file,imageDir+newFileName);
    }

    private String commomUpload(MultipartFile file,String imagePath){
        if (file.isEmpty()) {
            return "上传文件为空";
        }
        // 首先校验图片格式"jpg","jpeg", "png", "bmp", "gif"
        List<String> imageType = new ArrayList<>();
        imageType.add("jpg");
        imageType.add("jpeg");
        imageType.add("png");
        imageType.add("bmp");
        imageType.add("gif");
        // 获取文件名，带后缀
        String originalFilename = file.getOriginalFilename();
        //        System.out.println(originalFilename);
        // 获取文件的后缀格式
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (imageType.contains(fileSuffix)) {
            // 该方法返回的为当前项目的工作目录，即在哪个地方启动的java线程
            System.out.println(imagePath);
            File destFile = new File(imagePath);
            System.out.println(destFile);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            try {
                file.transferTo(destFile);
                // 将相对路径返回给前端
                return destFile.toString();
            } catch (IOException e) {
                System.out.println(e);
                return "上传失败";
            }
        } else {
            return "非法文件";
        }
    }

    public ResponseEntity<FileSystemResource> getHeadFile(String userid){
        String absDir = System.getProperty("user.dir");//找到当前文件启动目录
        String relDir= File.separator+"file"+File.separator+"headImage"+File.separator;
        String dir=absDir+relDir;
        //获取头像
        String fileName=userid+".jpg";
//        System.out.println(dir);
        File image=new File(dir+fileName);
        return getFile(image);
    }

    public ResponseEntity<FileSystemResource> getBgFile(String sharingid){
        String absDir = System.getProperty("user.dir");//找到当前文件启动目录
        String relDir= File.separator+"file"+File.separator+"backImage"+File.separator;
        String dir=absDir+relDir;
        //获取背景
        String fileName=sharingid+".jpg";
        File image=new File(dir+fileName);
        return getFile(image);
    }

    private ResponseEntity<FileSystemResource> getFile(File image) {
        System.out.println(image);
        //如果文件存在
        if (image.exists()) {
            //返回用户流
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(image));
        }else {
            //返回默认流
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new FileSystemResource(new File(image.getParent()+File.separator+"ic_app.jpg")));
        }
    }

}
