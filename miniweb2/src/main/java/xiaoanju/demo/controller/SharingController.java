package xiaoanju.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xiaoanju.demo.entity.Sharings;
import xiaoanju.demo.service.SharingService;

import javax.websocket.server.PathParam;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/sharing")
public class SharingController {

    @Autowired
    private SharingService sharingService;

    @GetMapping("/getUserSharings")
    public List<Sharings> getSharingById(@PathParam("userid") String userid){
        return sharingService.getSharingsByUserid(userid);
    }

    @GetMapping("/getAll")
    public List<Sharings> getAll(){
        return sharingService.getAll();
    }

    @PostMapping("/insertSharing")
    public Sharings insertOne(@RequestBody Sharings sharings){
        return sharingService.insert(sharings);
    }

    @DeleteMapping("/deleteAll")
    public String deleteAll(@PathParam("userid") String userid){
        sharingService.deleteAll(userid);
        return "账号为"+userid+"的用户信息全部被删除";
    }

    @DeleteMapping("/deleteOne")
    public String deleteOne(@PathParam("id")int id){
        String absDir = System.getProperty("user.dir");//找到当前文件启动目录
        String relDir= File.separator+"file"+File.separator+"backImage"+File.separator;
        String dir=absDir+relDir;
        //获取背景
        String fileName=id+".jpg";
        File image=new File(dir+fileName);
        //删除图片
        if (image.exists()) {
            image.delete();
        }
        int i = sharingService.deleteById(id);
        if (i>0) {
            return "id为" + id + "的分享被删除";
        }else
            return "该id不存在";
    }

    @PutMapping("/updateSharing")
    public Sharings updateSharings(@RequestBody Sharings sharings ){
        return sharingService.updateSharing(sharings);
    }

    @PutMapping("/addComNum")
    public Sharings updateSharings(@PathParam("id")int id){
        return sharingService.addComNum(id);
    }

}
