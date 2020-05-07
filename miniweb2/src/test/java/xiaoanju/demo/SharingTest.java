package xiaoanju.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xiaoanju.demo.entity.Sharings;
import xiaoanju.demo.mapper.SharingMapper;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class SharingTest {

    @Autowired
    private SharingMapper sharingMapper;

    @Test
    void deleteAll(){
        QueryWrapper<Sharings> wrapper=new QueryWrapper<>();
        wrapper.eq("userid","22222");
        sharingMapper.delete(wrapper);
    }

    @Test
    void insert(){
        Sharings sharings=new Sharings();
        sharings.setContent("adsfsdaf");
        sharings.setAuthor("sersdfdasd");
        sharings.setUserid("2222");
        sharingMapper.insert(sharings);
    }

    @Test
    void select(){
        QueryWrapper<Sharings> wrapper=new QueryWrapper<>();
        wrapper.eq("userid","2222");
        List<Sharings> sharings = sharingMapper.selectList(wrapper);
        for (Sharings sharing : sharings) {
            System.out.println(sharing);
        }
    }
}
