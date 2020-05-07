package com.example.dayrecord.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.Utils.MyDBHelper;
import com.example.dayrecord.R;
import com.example.dayrecord.Utils.NoQuickClick;
import com.example.dayrecord.server.InfoHub;


public class LoginActivity extends AppCompatActivity {

    private Button mBtnLogin;
    private EditText number,password;
    private TextView mTvRegister;

    private SharedPreferences mSp;
    private SharedPreferences.Editor mEdit;
    private CheckBox mCbRemPass,mCbAutoLogin;
    private EditText number_reg,pass_reg,nickName,reg_pass;

    //数据库相关
    private MyDBHelper myHelper;
    private SQLiteDatabase sqlite;
    private ContentValues values;

    private TextView mHideTips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalManager.getWriterPermission(this);//动态申请权限
        GlobalManager.init(this);

        setContentView(R.layout.activity_main);
        //找到相关组件
        mBtnLogin=findViewById(R.id.login_login);
        number=findViewById(R.id.login_user);
        password=findViewById(R.id.login_pass);
        mTvRegister=findViewById(R.id.login_reister);
//        mTvForgetPass=findViewById(R.id.login_fogetPass);
        mCbRemPass=findViewById(R.id.login_remPass);
        mCbAutoLogin=findViewById(R.id.login_autoLogin);

        //登陆界面的一些信息初始化
        mSp=getSharedPreferences("loginData",MODE_PRIVATE);
        mEdit = mSp.edit();

        //初始化数据库相关信息
        myHelper=new MyDBHelper(this, GlobalManager.DBNAEM,null,1);//指明版本方便数据库的更新
        sqlite=myHelper.getReadableDatabase();
        values=new ContentValues();

        //登陆过并且记住过密码
        if(mSp.getBoolean("HavingLogin",false) && mSp.getBoolean("rememberPassword",false)){
            mCbRemPass.setChecked(true);
            number.setText(mSp.getString("num","111"));
            password.setText(mSp.getString("pass","111"));

            //如果设置过自动登陆
            if(mSp.getBoolean("autoLogin",false)){
                mCbAutoLogin.setChecked(true);
                //自动登陆
                new InfoHub().loginInfoMatch(this,number.getText().toString(),
                        password.getText().toString(),mBtnLogin);
            }
        }

        //设置点击事件
        //登陆
        mBtnLogin.setOnClickListener(new NoQuickClick.OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Activity activity= (Activity) v.getContext();
                new InfoHub().loginInfoMatch(activity,number.getText().toString(),
                        password.getText().toString(), mBtnLogin);
            }
        });

        //注册
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                final View view= LayoutInflater.from(LoginActivity.this).inflate(R.layout.alertdialog_login_register,null);
                //找到注册界面的各个组件
                Button btnNext=view.findViewById(R.id.login_register_next);//下一步
                number_reg=view.findViewById(R.id.login_register_user);//账号
                pass_reg=view.findViewById(R.id.login_register_confirmPass);//二次密码
                reg_pass=view.findViewById(R.id.login_register_pass);//密码
                nickName=view.findViewById(R.id.login_register_name);
                mHideTips=view.findViewById(R.id.login_register_tips);

                //checkbox 监听输入的变化给出反馈
                final CheckBox cbName=view.findViewById(R.id.login_register_name_reflect);
                final CheckBox cbNum=view.findViewById(R.id.login_register_num_reflect);
                final CheckBox cbPass=view.findViewById(R.id.login_register_pass_reflect);
                final CheckBox cbSecondPass=view.findViewById(R.id.login_register_secondpass_reflect);

                //设置事件监听
                nickName.addTextChangedListener(new EditWatcher(1,cbName));
                number_reg.addTextChangedListener(new EditWatcher(2,cbNum));
                reg_pass.addTextChangedListener(new EditWatcher(3,cbPass));
                pass_reg.addTextChangedListener(new EditWatcher(4,cbSecondPass));

                final ImageView close=view.findViewById(R.id.login_register_x);
                builder.setView(view).setCancelable(false);
                final AlertDialog dialog=builder.show();

                //点x关闭窗口
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //点击下一步后，如果注册信息正确，销毁对话框，保存用户.
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //执行注册相关的操作
                        new InfoHub().registerRel(view,number_reg.getText().toString(),
                                reg_pass.getText().toString(),nickName.getText().toString(),dialog);
                    }
                });
            }
        });
    }

    class EditWatcher implements TextWatcher{
        private int type;//1昵称 2账号 3密码 4二次密码
        private CheckBox checkBox;
        private boolean isRegister;
        EditWatcher(int type, CheckBox checkBox) {
            this.type = type;
            this.checkBox = checkBox;
            isRegister=false;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //昵称、账号、密码均检验长度
            if (!lengthMatch(type, s.toString())) {
                checkBox.setChecked(false);
                if (isRegister) {
                    mHideTips.setVisibility(View.GONE);
                }
                return;
            }
            //此时昵称匹配
            if (type == 1) {
                checkBox.setChecked(true);
                return;
            }

            //账号检验内容
            if (!contentMatch(type, s.toString())) {
                checkBox.setChecked(false);
                return;
            }
            //此时账号密码匹配
            if (type == 3 || type == 2) {//此时密码已经匹配
                checkBox.setChecked(true);
            } else {
                //二次密码匹配
                if (confirmPassMatch(s.toString(), reg_pass.getText().toString())) {
                    checkBox.setChecked(true);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }

    //1 名称
    //2 账号密码
    //长度匹配
    private boolean lengthMatch(int type,String s){
        if(type==1){//名称1-10个字符
            return s.length()<10&&s.length()>1;
        }else //账号密码6-15
            return s.length()>=6&&s.length()<=15;
    }
    //内容匹配 //1昵称 2账号 3密码 4二次密码
    private boolean contentMatch(int type,String s){
        if(type==2){
            for (int i = 0; i <s.length() ; i++) {
                if(!Character.isDigit(s.charAt(i)))
                    return false;
            }
            return true;
        }else{
            for (int i = 0; i <s.length() ; i++) {
                if(!Character.isLetterOrDigit(s.charAt(i)))
                    return false;
            }
            return true;
        }
    }
    //二次密码匹配
    private boolean confirmPassMatch(String self,String pass ){
        return  self.equals(pass);
    }

    //对于动态申请权限的处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == 101) {
            //申请成功
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast toast=Toast.makeText(this, "你把照片给我交了", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            } else {
                Toast toast=Toast.makeText(this, "不交照片不给用", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2000);
            }

        }
    }


    //num,password,sex,signature,name
    //将注册的用户信息存入数据库
//    private void add(){
//        values.put("num",mNumber);
//        values.put("password",mPassword);
//        values.put("name",nickName.getText().toString());
//        values.put("headpath","");
//        sqlite.insert("user",null,values);
//        values.clear();
//    }

    //传递用户的账号以便获取用户的相关信息
//    private void jumpWithInfo(){
//        GlobalManager.readUserInfo(number.getText().toString());
//        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
//        startActivity(intent);
//        Toast.makeText(this,"欢迎您,"+GlobalManager.user.getNickname(),Toast.LENGTH_SHORT).show();
//    }

}
