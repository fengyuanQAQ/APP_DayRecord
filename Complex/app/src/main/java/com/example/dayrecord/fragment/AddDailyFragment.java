package com.example.dayrecord.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.Utils.MyDBHelper;
import com.example.dayrecord.R;
import com.example.dayrecord.data.DailyThings;

import java.util.ArrayList;
import java.util.List;

public class AddDailyFragment extends Fragment {
    private View mView;
    private String label;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_daily,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;
        label="";
        RadioButton rb=mView.findViewById(R.id.add_select_radioGroup_customed);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义标签
                final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                View view= LayoutInflater.from(getActivity()).inflate(R.layout.alertdialog_add_label,
                        null);
                builder.setView(view);
                final AlertDialog dialog=builder.show();
                ImageButton close=view.findViewById(R.id.add_select_dialog_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();//销毁窗口
                    }
                });
                ImageButton confirm=view.findViewById(R.id.add_select_dialog_confirm);
                final EditText labelContent=view.findViewById(R.id.add_select_dialog_content);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        label=labelContent.getText().toString();
                        if(!"".equals(label))
                            Toast.makeText(getActivity(), "预添加["+label+"]", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "预添加到[默认]", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void saveInfo(String time){
        //找到相关信息
        EditText edtTitle=mView.findViewById(R.id.add_daily_title);
        EditText edtContent=mView.findViewById(R.id.add_daily_text);
        RadioGroup rg=mView.findViewById(R.id.add_select_radioGroup);
        String title=edtTitle.getText().toString();
        String content=edtContent.getText().toString();
        //如果自定义了但是没有选中
        if(rg.getCheckedRadioButtonId()!=R.id.add_select_radioGroup_customed){
            RadioButton rb=mView.findViewById(rg.getCheckedRadioButtonId());
            label=rb.getText().toString();
        }else{
            //如果点击自定义但是并没有添加
            if("".equals(label)){
                label="默认";
            }
        }
        Toast.makeText(getContext(), "添加到["+label+"]", Toast.LENGTH_SHORT).show();

//        //添加到最近显示
//        String pos2=label+":"+GlobalManager.dailyThingsList.size();
//        DailyThings dailyThings1=new DailyThings(title,content,label,GlobalManager.user.getNum(),
//                time,pos2);
//        GlobalManager.dailyThingsList.add(dailyThings1);


        //保存到数据库中
        //存入数据库
        ContentValues values=new ContentValues();
        MyDBHelper myHelper=new MyDBHelper(getActivity(), GlobalManager.DBNAEM, null, 1);
        SQLiteDatabase sqlite=myHelper.getReadableDatabase();
        Cursor cursor=sqlite.query("daily",null,null,null,
                null,null,null);
        String pos;
        if(cursor.moveToLast()){
            String p1=cursor.getString(cursor.getColumnIndex("pos"));
            pos=label+":"+(Integer.parseInt(p1.substring(p1.indexOf(":")+1))+1);
            cursor.close();
        }else{
            //此时数据为空
            pos=label+":0";
            cursor.close();
        }
        values.put("content",content);
        values.put("label",label);
        values.put("title",title);
        values.put("num",GlobalManager.user.getId());
        values.put("shijian",time);
        values.put("pos",pos);
        sqlite.insert("daily",null,values);
        values.clear();

        //保存到全局缓冲数据
        DailyThings dailyThings=new DailyThings(title,content,label, GlobalManager.user.getId());
        dailyThings.setTime(time);
        dailyThings.setIdentity(pos);

        if(GlobalManager.dailyMap.containsKey(label)){
            //存在 label
            GlobalManager.dailyMap.get(label).add(dailyThings);
        }else{
            List<DailyThings> list=new ArrayList<>();
            list.add(dailyThings);
            GlobalManager.stringList.add(label);
            GlobalManager.dailyMap.put(label,list);
        }
    }

}
