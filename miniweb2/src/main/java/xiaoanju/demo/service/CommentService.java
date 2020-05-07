package xiaoanju.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaoanju.demo.entity.Comment;
import xiaoanju.demo.mapper.CommentMapper;

import javax.websocket.server.PathParam;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    //添加评论
    public void insertComment(Comment comment) {
        commentMapper.insert(comment);
    }

    //删除评论
    public int deleteComment(long id) {
        return commentMapper.deleteById(id);
    }

    //获取所有该分享的评论
    public List<Comment> getAll(long sharingid) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("sharingid", sharingid);
        return commentMapper.selectList(wrapper);
    }

    //根据id删除评论
    public void deleteCommentsById(int sharingid) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("sharingid", sharingid);
        commentMapper.delete(wrapper);
    }

    public void deleteCommentsByList(List<Integer> list) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        //仅当有参数时才执行
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                wrapper.eq("sharingid", list.get(i));
                if (i != list.size() - 1) {
                    wrapper.or();
                }
            }
            commentMapper.delete(wrapper);
        }
    }
}
