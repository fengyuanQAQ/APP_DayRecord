package com.example.dayrecord.adapter;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.Utils.MyDBHelper;
import com.example.dayrecord.R;
import com.example.dayrecord.data.DailyThings;
import com.example.dayrecord.fragment.ShowDailyFragment;


import java.util.List;
import java.util.Map;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder> {
    private ShowDailyFragment showDailyFragment;
    private Map<String, List<DailyThings>> map;
    private List<String> list;
    private boolean click[];

    public DailyAdapter(ShowDailyFragment showDailyFragment) {
        this.showDailyFragment = showDailyFragment;
        map = GlobalManager.dailyMap;
        list = GlobalManager.stringList;
        click=new boolean[getItemCount()];
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyViewHolder(LayoutInflater.from(showDailyFragment.getContext()).inflate(
                R.layout.adapter_fragment_show_daily_categories, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final DailyViewHolder holder, final int position) {
        if(getItemCount()==1||position==getItemCount()-1){
            //如果是第一项和最后一项 不显示分割线
            holder.divideView.setVisibility(View.GONE);
        }
        final String category = list.get(position);
        holder.mLable.setText(category);
        holder.mCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!click[position]) {
                    //没有被点击
                    //显示二级目录
                    holder.itemRecycler.setVisibility(View.VISIBLE);//设置视图可见
                    holder.itemRecycler.setAdapter(new DailyCategoryAdapter(showDailyFragment, category));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(showDailyFragment.getContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    holder.itemRecycler.setLayoutManager(linearLayoutManager);
                    holder.divideView.setVisibility(View.GONE);//隐藏分割线
                }else {
                    holder.itemRecycler.setVisibility(View.GONE);//隐藏
                    holder.divideView.setVisibility(View.VISIBLE);//显示分割线
                }
                click[position]=!click[position];//取反
                if(getItemCount()==1||position==getItemCount()-1){
                    //如果是第一项和最后一项 不显示分割线
                    holder.divideView.setVisibility(View.GONE);
                }
            }
        });
        holder.mCv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final PopupWindow popWnd=new PopupWindow();
                View view=LayoutInflater.from(showDailyFragment.getContext()).inflate(R.layout.popwnd_fragment_mine,null);
                popWnd.setContentView(view);
                popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

                //设置透明效果
                GlobalManager.setTransparent(showDailyFragment.getActivity(),
                        popWnd);

                //依附布局
                popWnd.setFocusable(true);
                popWnd.setOutsideTouchable(true);
                popWnd.setAnimationStyle(R.style.PopupWindowAnimation);
                popWnd.showAtLocation(view, Gravity.BOTTOM,0,0);

                //找到popwnd的相关组件
                Button btnCancel=view.findViewById(R.id.mine_popwnd_cancel);
                Button btnEdit=view.findViewById(R.id.mine_popwnd_edit);
                String str="删除["+category+"]";
                btnEdit.setText(str);
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
                        //清除全局缓存
                        GlobalManager.stringList.remove(category);
                        GlobalManager.dailyMap.remove(category);

//                        //清除最近添加
//                        for (DailyThings item: GlobalManager.dailyThingsList) {
//                            String s=item.getIdentity();
//                            //如果标签相同
//                            if(s.substring(0,s.indexOf(":")).equals(category)){
//                                GlobalManager.dailyThingsList.remove(item);
//                            }
//                        }

                        //清除数据库
                        MyDBHelper myHelper=new MyDBHelper(showDailyFragment.getContext(), GlobalManager.DBNAEM, null, 1);
                        SQLiteDatabase sqlite=myHelper.getReadableDatabase();
                        sqlite.delete("daily","label=?",new String[]{category});
                        //重新加载
                        showDailyFragment.onResume();
                    }
                });
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        //目录的长度
        return list.size();
    }

    class DailyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView itemRecycler;
        CardView mCv;
        TextView mLable;
        View divideView;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCv=itemView.findViewById(R.id.show_daily_categories_cardview);
            mLable=itemView.findViewById(R.id.show_daily_categories);
            divideView=itemView.findViewById(R.id.show_daily_categories_divider);
            itemRecycler = itemView.findViewById(R.id.show_daily_recycler_innner);
        }
    }
}
