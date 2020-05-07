package com.example.dayrecord.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayrecord.R;
import com.example.dayrecord.data.Comment;
import com.example.dayrecord.server.InfoHub;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.InnerViewHolder>  {

    private Context mContext;
    private List<Comment> mCommentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        mContext = context;
        mCommentList = commentList;
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_activity_comment_item
        ,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        //如果是最后一条评论消除分界线
        if(position==mCommentList.size()-1){
            View divider = holder.itemView.findViewById(R.id.discover_comment_divider);
            divider.setVisibility(View.GONE);
        }

        Comment comment=mCommentList.get(position);

        //获取id
        int sharingid=comment.getSharingid();

        //获取用户名
        String name=comment.getNickname();
        //获取时间
        String time=comment.getTime();
        //获取内容
        String content=comment.getContent();

        //时间
        TextView tvTime = holder.itemView.findViewById(R.id.discover_comment_time);
        //内容
        TextView tvContent = holder.itemView.findViewById(R.id.discover_comment_content);
        //头像 用户名
        ImageView ivHead=holder.itemView.findViewById(R.id.discover_comment_headimage);
        TextView userName=holder.itemView.findViewById(R.id.discover_comment_name);

        //加载头像
        new InfoHub().getSharingHeadImage((Activity) mContext,comment.getUserid(),ivHead);

        //处理时间 如果是当日则输出具体时间，如果不是只需要输出月份
        //获取当前时间
        String currentTime= new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
//        Toast.makeText(mContext, truncYMD(currentTime), Toast.LENGTH_SHORT).show();
        if (truncYMD(time).equals(truncYMD(currentTime))) {
            //是当日
            tvTime.setText(truncHM(time));
        }else
            tvTime.setText(truncMD(time));

        //设置
        userName.setText(name);
        tvContent.setText(content);

    }

    //截取年月日
    private String truncYMD(String time){
        return time.substring(0,time.indexOf("-"));
    }
    //截取月日
    private String truncMD(String time){
        String m = time.substring(4, 6);
        String d=time.substring(6,time.indexOf("-"));
        return m+"-"+d;
    }
    //截取小时分
    private String truncHM(String time){
        int pos=time.indexOf("-")+1;
        String h = time.substring(pos, pos + 2);
        String d=time.substring(pos+2,pos+4);
        return h+":"+d;
    }

   @Override
    public int getItemCount() {
        return mCommentList.size();
    }


    class InnerViewHolder extends RecyclerView.ViewHolder{
        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
