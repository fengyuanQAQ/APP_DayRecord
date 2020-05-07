package com.example.dayrecord.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.R;
import com.example.dayrecord.activity.EditActivity;
import com.example.dayrecord.activity.SettingActivity;
import com.example.dayrecord.activity.ShowActivity;


public class MineFragment extends Fragment {

    //组件
    private TextView mTvName,mTvSignature;
    private PopupWindow popWnd;
    private ImageView mIvHead;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //找到昵称 签名 头像 daily跳转，share跳转
        mTvName=view.findViewById(R.id.mine_fragment_nickName);
        mTvSignature=view.findViewById(R.id.mine_fragment_signature);
        mIvHead=view.findViewById(R.id.mine_fragment_head);
        ImageView mIvSetting = view.findViewById(R.id.mine_fragment_setting);
        //显示分享字句和日常页面
        CardView mLlShare = view.findViewById(R.id.mine_fragment_share);
        CardView mLlDaily = view.findViewById(R.id.mine_fragment_daily);

        //监听事件
        mTvSignature.setOnClickListener(new OnClickThis());
        mTvName.setOnClickListener(new OnClickThis());
        mIvHead.setOnClickListener(new OnClickThis());
        mIvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        mLlDaily.setOnClickListener(new OnClickNext());
        mLlShare.setOnClickListener(new OnClickNext());

    }

    class OnClickThis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            View view=LayoutInflater.from(getContext()).inflate(R.layout.popwnd_fragment_mine,null);
            popWnd=new PopupWindow();
            popWnd.setContentView(view);
            popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

            //设置透明效果
            setTransparent();

            //依附布局
            popWnd.setFocusable(true);
            popWnd.setOutsideTouchable(true);
            popWnd.setAnimationStyle(R.style.PopupWindowAnimation);
            popWnd.showAtLocation(view, Gravity.BOTTOM,0,0);

            //找到popwnd的相关组件
            Button btnCancel=view.findViewById(R.id.mine_popwnd_cancel);
            Button btnEdit=view.findViewById(R.id.mine_popwnd_edit);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popWnd.dismiss();
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popWnd.dismiss();
                    Intent intent=new Intent(getActivity(), EditActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    class OnClickNext implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getActivity(),ShowActivity.class);
            Bundle bundle=new Bundle();
            if(v.getId()==R.id.mine_fragment_share){
                bundle.putInt("showwhat",1);//1代表显示share
            }else{
                bundle.putInt("showwhat",2);//2代表显示daily
            }
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void setTransparent(){
        final Window window=getActivity().getWindow();
        final WindowManager.LayoutParams lp=window.getAttributes();
        lp.alpha=0.7f;//设置透明度
        window.setAttributes(lp);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //恢复此时的透明度
                lp.alpha=1f;
                window.setAttributes(lp);
            }
        });
    }

    //更新刚才编辑的信息
    @Override
    public void onResume(){
        super.onResume();
        loadUserInfo();
    }
    private void loadUserInfo(){
        String sig= GlobalManager.user.getSignature();
//        String mImagePath = GlobalManager.user.getHeadimage();
        String mName = GlobalManager.user.getNickname();
        if("".equals(sig)){
            sig="暂无签名";
            mTvSignature.setTextColor(Color.GRAY);
        }
        mTvSignature.setText(sig);
        mTvName.setText(mName);
//        GlobalManager.loadHeadImage(getActivity(),mIvHead, mImagePath);

//        new InfoHub().getHeadImage(getActivity(),GlobalManager.user.getId(),
//                mIvHead);

        //加载头像
        Glide.with(getActivity()).load(GlobalManager.headBytes).into(mIvHead);

        View view=getView();
        //显示长度
        assert view != null;
        TextView mTvShareNum=view.findViewById(R.id.mine_fragment_share_number);
        String str1=GlobalManager.sharingList.size()+"字句";
        mTvShareNum.setText(str1);
        TextView mTvDailyNum=view.findViewById(R.id.mine_fragment_daily_number);
        String str2;
        int count=0;
        for (String label:
             GlobalManager.stringList) {
            count+=GlobalManager.dailyMap.get(label).size();
        }
        str2=count+"待办";
        mTvDailyNum.setText(str2);
    }

}
