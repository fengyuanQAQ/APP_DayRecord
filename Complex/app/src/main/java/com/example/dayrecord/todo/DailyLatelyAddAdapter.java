package com.example.dayrecord.todo;

import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.Utils.MyDBHelper;
import com.example.dayrecord.R;
import com.example.dayrecord.data.DailyThings;
import com.example.dayrecord.fragment.ShowDailyFragment;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DailyLatelyAddAdapter extends RecyclerView.Adapter<DailyLatelyAddAdapter.ItemViewHolder> {
    private ShowDailyFragment showDailyFragment;
    private boolean notClick;
    private List<DailyThings> dailyThingsList;
    public DailyLatelyAddAdapter(ShowDailyFragment showDailyFragment) {
        this.showDailyFragment = showDailyFragment;
        notClick=false;//初始显示
//        dailyThingsList=GlobalManager.dailyThingsList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(showDailyFragment.getContext()).inflate(
                R.layout.adapter_fragment_show_daily_categories_item,parent,false
        ));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        DailyThings dailyThings=dailyThingsList.get(position);
        String content=dailyThings.getmContent();
        final String pos=dailyThings.getIdentity();
        String time=dailyThings.getTime();
        holder.mTvContent.setText(content);
        //提取时间
        final String day=time.substring(6,8);
        String month=time.substring(4,6);
        String hour=time.substring(9,11);
        String min=time.substring(11,13);
        final String year=time.substring(0,4);
        String[] weekTable ={"周日","周一","周二","周三","周四","周五","周六"};
        Calendar calendar= Calendar.getInstance();
        final Bundle bundle=new Bundle();
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
            Toast.makeText(showDailyFragment.getActivity(), "解析时间失败", Toast.LENGTH_SHORT).show();
        }

        //长按删除事件
        holder.mCv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final PopupWindow popupWindow=new PopupWindow();
                View view=LayoutInflater.from(showDailyFragment.getContext()).inflate(
                        R.layout.popwnd_activity_show_share_dropdown,null);
                popupWindow.setContentView(view);
                popupWindow.setWidth(holder.mCv.getWidth()/4);
                popupWindow.setHeight(450);
                GlobalManager.setTransparent(showDailyFragment.getActivity(),popupWindow);
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
                        popupWindow.dismiss();
                        deleteItem(pos);
                        showDailyFragment.onResume();
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dailyThingsList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView mTvContent;
        CardView mCv;
        TextView mTvDay,mTvMonthAndWeek,mTvHourAndMin;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvContent=itemView.findViewById(R.id.show_daily_categories_item_content);
            mCv=itemView.findViewById(R.id.show_daily_categories_item_cardview);
            mTvDay=itemView.findViewById(R.id.show_daily_day);
            mTvMonthAndWeek=itemView.findViewById(R.id.show_daily_date);
            mTvHourAndMin=itemView.findViewById(R.id.show_daily_time);
        }
    }

    private void deleteItem(String pos){
        int index=Integer.parseInt(pos.substring(pos.indexOf(":")+1));
        //删除全局缓存
        dailyThingsList.remove(index);

        String label=pos.substring(0,pos.indexOf(":"));
        GlobalManager.dailyMap.get(label).remove(index);
        //删除数据库
        MyDBHelper myHelper=new MyDBHelper(showDailyFragment.getContext(), GlobalManager.DBNAEM, null, 1);
        SQLiteDatabase sqlite=myHelper.getReadableDatabase();
        sqlite.delete("daily","pos=?",new String[]{pos});
    }
}
