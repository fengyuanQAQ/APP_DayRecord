package xiaoanju.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;


@Getter
@Setter
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("sharingcomment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private long id;
    private long num;
    private String content;
    private String userid;
    private String time;

    private String nickname;
    private long sharingid;
    private String headimage;

}
