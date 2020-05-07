package com.example.dayrecord.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.dayrecord.R;


public class SettingActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //管理偏好设置
        SharedPreferences mSp = getSharedPreferences("loginData", MODE_PRIVATE);
        editor= mSp.edit();

        Switch mSwichAuto = findViewById(R.id.mine_setting_autologin);
        mSwichAuto.setChecked(mSp.getBoolean("autoLogin",false));
        ImageView mIvBack = findViewById(R.id.mine_setting_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSwichAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("autoLogin",isChecked);
                editor.apply();
            }
        });
        Button btnQuit=findViewById(R.id.mine_setting_quit);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("确认退出").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SettingActivity.this,LoginActivity.class).
                                setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        SharedPreferences sp=getSharedPreferences("loginData",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putBoolean("rememberPassword",false);
                        editor.apply();
                        finish();
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(true).show();
            }
        });
    }

}
