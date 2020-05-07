package xiaoanju.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xiaoanju.demo.entity.Comment;
import xiaoanju.demo.service.CommentService;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/getComments")
    public List<Comment> getAll(@PathParam("sharingid") long sharingid){
        return commentService.getAll(
                sharingid);
    }

    @PostMapping("/insert")
    public Comment insert(@RequestBody Comment comment){
        commentService.insertComment(comment);
        return comment;
    }

    @DeleteMapping("/delete")
    public String delete(@PathParam("id") long id){
        int i = commentService.deleteComment(id);
        String result=null;
        if (i<=0) {
            result="当前没有元素";
        }else
            result="删除了一条评论";
        return result;
    }

    @DeleteMapping("/deleteBySharingId")
    public String deleteBySharingId(@PathParam("sharingid")int sharingid){
        commentService.deleteCommentsById(sharingid);
        return "删除了所有id为"+sharingid+"的记录";
    }

    @DeleteMapping("/deleteByList")
    public String deleteBySharingId(@RequestParam("sharingids") ArrayList<Integer> list ){
        commentService.deleteCommentsByList(list);
        return "删除了所有id的记录";
    }


}
