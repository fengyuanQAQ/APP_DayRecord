package xiaoanju.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@TableName("userinfo")
public class User {
    private String id;
    private String password;
    private String nickname;
    private String signature;
    private String headimage;
}
