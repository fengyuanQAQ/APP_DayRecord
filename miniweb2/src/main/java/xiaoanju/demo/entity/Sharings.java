package xiaoanju.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("sharings")
public class Sharings {
    @TableId(type = IdType.AUTO)
    private long id;
    private long num;
    private long commentnum;
    private String content;
    private String author;
    private String userid;
    private String time;
    private String nickname;
    private String image;
    private String headimage;
}
