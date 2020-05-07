package com.example.dayrecord.Utils;

import android.view.View;

import java.util.Calendar;

public class NoQuickClick {
    public static abstract class OnNoDoubleClickListener implements View.OnClickListener{
        private  int period=1000;//点击间隔
        private  long previousTime;//之前的计时
        @Override
        public void onClick(View v) {
            long currentTime= Calendar.getInstance().getTimeInMillis();
            if(currentTime-previousTime>=period){
                //如果间隔时间>1s
                previousTime=currentTime;
                onNoDoubleClick(v);//执行非快速点击
            }
        }
        public abstract void onNoDoubleClick(View v);
    }
}
