package com.example.dayrecord.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.R;
import com.example.dayrecord.Utils.MyDBHelper;
import com.example.dayrecord.Utils.NoQuickClick;
import com.example.dayrecord.activity.CommentActivity;
import com.example.dayrecord.data.Sharings;
import com.example.dayrecord.fragment.DiscoverFragment;
import com.example.dayrecord.server.InfoHub;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.LinearViewHolder> {
    private DiscoverFragment mDiscoverFragment;
    private List<Sharings> mListShare;
    private InfoHub mInfoHub;
    private Activity mActivity;

    public DiscoverAdapter(DiscoverFragment discoverFragment, List<Sharings> listShare) {
        this.mDiscoverFragment = discoverFragment;
        mListShare = listShare;
        mInfoHub = new InfoHub();
        mActivity = discoverFragment.getActivity();
    }

    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mDiscoverFragment.getContext()).inflate(
                R.layout.adapter_fragment_discover_recycler_item, parent, false
        ));
    }


    @Override
    public void onBindViewHolder(@NonNull final LinearViewHolder holder, final int position) {
        //从服务器端获取数据
        Sharings sharingThings = mListShare.get(position);
        //提取信息
        long id = sharingThings.getId();
        int num = sharingThings.getNum();
        int commentnum = sharingThings.getCommentnum();
        String userid = sharingThings.getUserid();
        String name = sharingThings.getNickname();

        String content = sharingThings.getContent();
        String author = sharingThings.getAuthor();

        //加载头像
        new InfoHub().getSharingHeadImage(mActivity,userid,holder.mIvHead);
        //加载图片
        if (!"".equals(sharingThings.getImage())) {
            new InfoHub().getBgImage(mActivity,sharingThings,holder.mIvImage);
        }else
            holder.ll.removeView(holder.mIvImage);
        holder.mTvName.setText(name);
        //设置点赞数量

        //设置时间

        holder.mTvContent.setText(content);
        author = "—" + author + "—";
        holder.mTvAuthor.setText(author);

        String time = sharingThings.getTime();

        //处理时间 如果是当日则输出具体时间，如果不是只需要输出月份
        //获取当前时间
        String currentTime= new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
//        Toast.makeText(mContext, truncYMD(currentTime), Toast.LENGTH_SHORT).show();
        if (truncYMD(time).equals(truncYMD(currentTime))) {
            //是当日
            holder.mTvTime.setText(truncHM(time));
        }else
            holder.mTvTime.setText(truncMD(time));

        //设置点赞和评论事件
        ImageView tvStar = holder.itemView.findViewById(R.id.discover_star);
        TextView tvStarNum = holder.itemView.findViewById(R.id.discover_starnum);
        ImageView tvComment = holder.itemView.findViewById(R.id.discover_comment);
        TextView tvCommentNum = holder.itemView.findViewById(R.id.discover_comment_commentnum);

        tvCommentNum.setText(String.valueOf(commentnum));//评论数
        tvStarNum.setText(String.valueOf(sharingThings.getNum()));//点赞数
        //图片显示
        if (isClickStar((int) id, GlobalManager.user.getId())) {
            tvStar.setImageResource(R.mipmap.ic_discover_star);
        } else {
            tvStar.setImageResource(R.mipmap.ic_discover_star2);
        }

        //点赞事件
        tvStar.setOnClickListener(new NoQuickClick.OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //test
//                printDB();

                //检测本地数据库是否点击过
                if (isClickStar((int) id, GlobalManager.user.getId())) {
                    //减少点赞数
                    int num1 = sharingThings.getNum();
                    num1 -= 1;
                    sharingThings.setNum(num1);
//                    Log.d("111", String.valueOf(num1));
                    tvStarNum.setText(String.valueOf(num1));
                    tvStar.setImageResource(R.mipmap.ic_discover_star2);
                    //取消数据库标记
                    cancelStarMark((int) id, GlobalManager.user.getId());
                } else {
                    int num1 = sharingThings.getNum();
                    num1 += 1;
                    sharingThings.setNum(num1);
                    //增加当前点赞数
                    tvStarNum.setText(String.valueOf(num1));
//                    Log.d("111", String.valueOf(num1));
                    tvStar.setImageResource(R.mipmap.ic_discover_star);
                    //同步点击标记到本地数据库
                    addStarMark((int) id, GlobalManager.user.getId());
                }
//                Log.d("111", "-----------");
//                printDB();

                //更新数据库
                new InfoHub().updateShaings(mActivity, sharingThings);
            }
        });

        tvComment.setOnClickListener(new NoQuickClick.OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //跳转到评论界面 传递当前用户的id
                Intent intent = new Intent(mActivity, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", id);
                bundle.putString("userid", userid);
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
            }
        });

        //最后一个 添加刷新按钮
        if (position == getItemCount() - 1) {
            //显示刷新栏
            LinearLayout linearLayout = holder.itemView.findViewById(R.id.discover_flushll);
            linearLayout.setVisibility(View.VISIBLE);
            ImageButton ibFlush = holder.itemView.findViewById(R.id.discover_hide);
            //设置刷新事件
            ibFlush.setOnClickListener(new NoQuickClick.OnNoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
//                    mDiscoverFragment.onResume();
                    mInfoHub.loadSharings(mDiscoverFragment,mDiscoverFragment.getRecycler());
                    //添加点击刷新动画
                    ibFlush.animate()
                            .rotation(360f)
                            .setDuration(200)
                            .start();
                }
            });
        }
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
        return mListShare.size();
    }

    /**
     * 判断当前用户是否对该分享点击
     */
    private boolean isClickStar(int id, String userid) {
        MyDBHelper myHelper = new MyDBHelper(mActivity, GlobalManager.DBNAEM, null, 1);
        SQLiteDatabase seqlite = myHelper.getReadableDatabase();
        Cursor cursor = seqlite.query("sharing_stars", null, null, null, null
                , null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                //先判断账号再判断标记
                if (userid.equals(cursor.getString(cursor.getColumnIndex("userid")))) {
                    if (id == cursor.getInt(cursor.getColumnIndex("mark"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 添加点赞标记
     */
    private void addStarMark(int id, String userid) {
        MyDBHelper myHelper = new MyDBHelper(mActivity, GlobalManager.DBNAEM, null, 1);
        SQLiteDatabase seqlite = myHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("mark", id);
        values.put("userid", userid);
        seqlite.insert("sharing_stars", null, values);
        values.clear();
        seqlite.close();
    }

    /**
     * 取消数据库标记
     */
    private void cancelStarMark(int id, String userid) {
        MyDBHelper myHelper = new MyDBHelper(mActivity, GlobalManager.DBNAEM, null, 1);
        SQLiteDatabase seqlite = myHelper.getReadableDatabase();
        seqlite.delete("sharing_stars", "mark=? and userid=?", new String[]{String.valueOf(id), userid});
        seqlite.close();
    }


    class LinearViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvImage;
        TextView mTvContent;

        //用户
        ImageView mIvHead;
        TextView mTvName;
        TextView mTvTime, mTvAuthor;

        LinearLayout ll;

        LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.discover_image);
            mTvContent = itemView.findViewById(R.id.discover_content);
            mTvAuthor = itemView.findViewById(R.id.discover_author);
            mTvTime = itemView.findViewById(R.id.discover_time);
            mIvHead = itemView.findViewById(R.id.discover_headimage);
//            mIvDropdown = itemView.findViewById(R.id.discover_dropdown);
            mTvName = itemView.findViewById(R.id.discover_name);
            ll = itemView.findViewById(R.id.discover_ll);
        }
    }
}
