package com.example.dayrecord.adapter;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.R;
import com.example.dayrecord.activity.BrowseShareItemActivity;
import com.example.dayrecord.activity.ShowActivity;
import com.example.dayrecord.data.Sharings;
import com.example.dayrecord.data.User;
import com.example.dayrecord.fragment.ShowShareFragment;
import com.example.dayrecord.server.InfoHub;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter. LinearViewHolder > implements ShowActivity.OnDelete {

    private List<Sharings> mList;
    private User user;
    private ShowShareFragment showShareFragment;
    private InfoHub mInfoHub;
    private RecyclerView mRecyclerView;

    public ShareAdapter(ShowShareFragment showShareFragment, RecyclerView recyclerView) {
        this.showShareFragment = showShareFragment;
        mList= GlobalManager.sharingList;
        user= GlobalManager.user;
        mInfoHub=new InfoHub();
        mRecyclerView=recyclerView;
    }

    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(showShareFragment.getContext()).inflate(R.layout.adapter_activity_show_fragment_share_recycler_item,
                parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull  final LinearViewHolder holder, final int position) {
        //最后一个 添加End
        if(position==getItemCount()-1){
            holder.mTvHide.setVisibility(View.VISIBLE);
        }else if(position<0){
            holder.mTvHide.setVisibility(View.VISIBLE);
        }
       Sharings sharingThings=mList.get(position);
        //提取信息
        String imagePath=sharingThings.getImage();
        String content=sharingThings.getContent();
        String author=sharingThings.getAuthor();
        String time=sharingThings.getTime();
        if(!"".equals(imagePath))
            GlobalManager.loadHeadImage(showShareFragment.getActivity(),holder.mIvBtn,imagePath);
        else
            holder.linearLayout.removeView(holder.mIvBtn);
        if(!"".equals(content))
            holder.mTvContent.setText(content);
        else
            holder.mTvContent.setText("记录生活");

        //提取时间
        final String day=time.substring(6,8);
        String month=time.substring(4,6);
        String hour=time.substring(9,11);
        String min=time.substring(11,13);
        final String year=time.substring(0,4);
        String[] weekTable ={"周日","周一","周二","周三","周四","周五","周六"};
        Calendar calendar= Calendar.getInstance();
        final  Bundle bundle=new Bundle();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd-HHmmss");
        try {
            Date date=simpleDateFormat.parse(time);
            calendar.setTime(date);
            int weekIndex=calendar.get(Calendar.DAY_OF_WEEK)-1;
            String week=weekTable[weekIndex];
            holder.mTvDay.setText(String.valueOf(Integer.parseInt(day)));
            String monthAndWeek= Integer.parseInt(month) +"月/"+week;
            holder.mTvMonthAndWeek.setText(monthAndWeek);
            String hourAndMin= Integer.parseInt(hour) +":"+min;
            holder.mTvHourAndMin.setText(hourAndMin);
            bundle.putString("time",hourAndMin);
            String d=year+"年"+monthAndWeek;
            bundle.putString("date",d);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(showShareFragment.getActivity(), "1111", Toast.LENGTH_SHORT).show();
        }

        //设置点击事件
        //点击卡片
        holder.mCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将sharingThing传递过去,通过pos
                Intent intent=new Intent(showShareFragment.getActivity(), BrowseShareItemActivity.class);
                bundle.putInt("index",position);
                bundle.putString("day",day);
                bundle.putString("year",year);
                intent.putExtras(bundle);
                showShareFragment.getActivity().startActivity(intent);
            }
        });
        //长按 长按删除
        holder.mCv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final PopupWindow popupWindow=new PopupWindow();
                View view=LayoutInflater.from(showShareFragment.getContext()).inflate(
                        R.layout.popwnd_activity_show_share_dropdown,null);
                popupWindow.setContentView(view);
                popupWindow.setWidth(holder.mCv.getWidth()/4);
                popupWindow.setHeight(450);
                GlobalManager.setTransparent(showShareFragment.getActivity(),popupWindow);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(holder.mCv, (holder.mCv.getWidth()-popupWindow.getWidth())/2,-holder.
                        mCv.getHeight()/2);

                Button btnDelte=view.findViewById(R.id.show_share_dropdown_delete);
                final Button btnCancel=view.findViewById(R.id.show_share_dropdown_cancle);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                btnDelte.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteCurrent(position);
                        popupWindow.dismiss();
                    }
                });
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size() ;
    }

    @Override
    public void deleteCurrent(int pos) {

        int id=GlobalManager.sharingList.get(pos).getId();

        //清除全局缓存中的元素
        GlobalManager.sharingList.remove(pos);

        //删除数据库里的元素（包括该该条分享的所有评论）
        mInfoHub.deleteOneUserSharings(showShareFragment.getActivity(),id);


        //加载元素
        mRecyclerView.setAdapter(new ShareAdapter(showShareFragment,mRecyclerView));

    }

    class  LinearViewHolder  extends RecyclerView.ViewHolder{

        CardView mCv;
        TextView mTvDay,mTvMonthAndWeek,mTvContent,mTvHourAndMin;
        LinearLayout linearLayout;//动态删除image
        ImageButton mIvBtn;

        TextView mTvHide;//之后最后才会显示

        LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mCv=itemView.findViewById(R.id.show_share_cardview);
            mIvBtn=itemView.findViewById(R.id.show_share_image);
            mTvDay=itemView.findViewById(R.id.show_share_day);
            mTvMonthAndWeek=itemView.findViewById(R.id.show_share_monthAndweek);
            mTvContent=itemView.findViewById(R.id.show_share_content);
            mTvHourAndMin=itemView.findViewById(R.id.show_share_hourAndmin);
            linearLayout=itemView.findViewById(R.id.show_share_ll);
            mTvHide =itemView.findViewById(R.id.show_share_hide);
        }
    }
}
