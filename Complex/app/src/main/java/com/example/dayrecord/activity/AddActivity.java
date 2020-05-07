package com.example.dayrecord.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dayrecord.R;
import com.example.dayrecord.Utils.NoQuickClick;
import com.example.dayrecord.fragment.AddDailyFragment;
import com.example.dayrecord.fragment.AddShareFragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    //不同便签显示不同内容
    private AddDailyFragment addDailyFragment;
    private AddShareFragment addShareFragment;

    //保存的图片的路径
    private String imagePath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //找到相关组件
        RadioGroup mRgTitle = findViewById(R.id.add_at_title);
        ImageButton mIvBack = findViewById(R.id.add_at_back);
        TextView mTvNext = findViewById(R.id.add_at_next);

        //先显示的是daily便签
        addDailyFragment=new AddDailyFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.add_framelayout,addDailyFragment,"daily").commitAllowingStateLoss();
        //添加返回和保存时间
        mTvNext.setOnClickListener(new NoQuickClick.OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //将内容存储到数据库里面
                RadioButton rbDaily=findViewById(R.id.add_title_daily);
                String time = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
//                Log.i("mxy", time);
                if(rbDaily.isChecked()){
                    EditText edtContent=findViewById(R.id.add_daily_text);
                    if (!"".equals(edtContent.getText().toString())) {
                        AddDailyFragment addDailyFragment= (AddDailyFragment) getSupportFragmentManager().findFragmentByTag("daily");
                        assert addDailyFragment != null;
                        addDailyFragment.saveInfo(time);
                        finish();
                    }else
                        Toast.makeText(AddActivity.this, "记录点什么吧", Toast.LENGTH_SHORT).show();
                }else{
                    AddShareFragment addShareFragment= (AddShareFragment) getSupportFragmentManager().findFragmentByTag("share");
                    EditText edtContent=findViewById(R.id.add_share_content);
//                    Toast.makeText(AddActivity.this, edtContent.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (!"".equals(edtContent.getText().toString())) {
                        //内容为空添加内容
                        addShareFragment.saveInfo(time,imagePath);
                    }else
                        Toast.makeText(AddActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mIvBack.setOnClickListener(this);

        //选择添加的便签类型
        mRgTitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.add_title_daily:
                        getSupportFragmentManager().beginTransaction().replace(R.id.add_framelayout,
                              addDailyFragment,"daily").commitAllowingStateLoss();
                        break;

                    case R.id.add_title_share:
                        if(addShareFragment==null){
                            addShareFragment=new AddShareFragment();
                        }
                       getSupportFragmentManager().beginTransaction().replace(R.id.add_framelayout,
                                addShareFragment,"share").commitAllowingStateLoss();
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    //获取图片的路径
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(this, String.valueOf(requestCode), Toast.LENGTH_SHORT).show();
        if(requestCode==1&&resultCode==RESULT_OK){
            ImageView image=getSupportFragmentManager().findFragmentByTag("share").getView().findViewById(R.id.add_share_image);
            Uri uri=data.getData();
            imagePath=getRealPathFromURI(uri);
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //根据uri获取filepath
    private String getRealPathFromURI(Uri contentUri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(column_index);
        }
        if (cursor != null) {
            cursor.close();
        }
        return result;
    }

}
