package xiaoanju.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaoanju.demo.entity.Sharings;
import xiaoanju.demo.mapper.SharingMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class SharingService {

    @Autowired
    private SharingMapper sharingMapper;

    //根据id删除单个分享
    public int deleteById(long id){
        String absDir = System.getProperty("user.dir");

        return sharingMapper.deleteById(id);
    }

    //删除该用户的所有分享 同时删除该分享的图片
    public void deleteAll(String userid){
        QueryWrapper<Sharings> wrapper=new QueryWrapper<>();
        wrapper.eq("userid",userid);
        List<Sharings> sharings = sharingMapper.selectList(wrapper);
        for (Sharings sharing : sharings) {
            String absDir = System.getProperty("user.dir");//找到当前文件启动目录
            String relDir= File.separator+"file"+File.separator+"backImage"+File.separator;
            String dir=absDir+relDir;
            //获取背景
            String fileName=sharing.getId()+".jpg";
            File image=new File(dir+fileName);
            deleteImage(image);
        }

        sharingMapper.delete(wrapper);
    }
    private void deleteImage(File file){
        if (file.exists()) {
            file.delete();
        }
    }

    //新增分享
    public Sharings insert(Sharings sharings){
        sharingMapper.insert(sharings);
        //返回自动增一的主键id
        return sharingMapper.selectById(sharings.getId());
    }

    //查找所有分享
    public List<Sharings> getAll(){
        return sharingMapper.selectList(null);
    }

    //查找该用户的所有分享
    public List<Sharings> getSharingsByUserid(String userid){
        QueryWrapper<Sharings> wrapper=new QueryWrapper<>();
        wrapper.eq("userid",userid);
        return sharingMapper.selectList(wrapper);
    }

    //更新分享
    public Sharings updateSharing(Sharings sharings){
         sharingMapper.updateById(sharings);
         return sharings;
    }

    //增加评论数量
    public Sharings addComNum(int id){
        Sharings sharings = sharingMapper.selectById(id);
        sharings.setCommentnum(sharings.getCommentnum()+1);
        sharingMapper.updateById(sharings);
        return sharings;
    }
}
