package com.example.dayrecord.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.R;
import com.example.dayrecord.data.User;
import com.example.dayrecord.server.InfoHub;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvHead;
    private EditText mEdtName, mEdtSig;
    private Uri mUri;

    //打开相机
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_OPEN_PHOTOS = 2;

    private PopupWindow popWnd;

    //用户数据
    private String mImagePath="";
    private String mNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //找到相关组件
        ImageView mIvClose = findViewById(R.id.mine_edit_close);
        mIvHead = findViewById(R.id.mine_edit_headimage);
        mEdtName = findViewById(R.id.mine_edit_name);
        mEdtSig = findViewById(R.id.mine_edit_signature);
        TextView mTvFinish = findViewById(R.id.mine_edit_finish);

        //设置点击事件
        mIvClose.setOnClickListener(this);
        mIvHead.setOnClickListener(this);
        mTvFinish.setOnClickListener(this);

        //初始化用户信息
        userInit();
    }

    //加载用户相关数据
    private void userInit() {
        mNum = GlobalManager.user.getId();
        mEdtName.setText(GlobalManager.user.getNickname());
        mEdtSig.setText(GlobalManager.user.getSignature());
        mImagePath= GlobalManager.user.getHeadimage();
//        GlobalManager.loadHeadImage(this, mIvHead, mImagePath);
//        new InfoHub().getHeadImage(this,GlobalManager.user.getId());
        Glide.with(this).load(GlobalManager.headBytes).into(mIvHead);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_edit_close:
                finish();
                break;

            case R.id.mine_edit_finish:
//                updateInfo();
                String name = mEdtName.getText().toString();
                String sig = mEdtSig.getText().toString();

                //当有信息变化时才更新
                if(!name.equals(GlobalManager.user.getNickname())
                    ||!sig.equals(GlobalManager.user.getSignature())
                    ||!mImagePath.equals(GlobalManager.user.getHeadimage())){
                    String password=GlobalManager.user.getPassword();
                    User user=new User(mNum,password,name,sig,mImagePath);

                    File image=new File(mImagePath);

                    //如果头像有改变的话提交头像更新数据库
                    if (!mImagePath.equals(GlobalManager.user.getHeadimage())) {
                        new InfoHub().uploadImage(this,image,user);
                    }else
                        //更新数据库信息
                        new InfoHub().updateUser(this,user);//只更新数据库
                }else
                    finish();
                break;

            case R.id.mine_edit_headimage:
                headImageEdit();
                break;
        }
    }

    //更新数据
//    private void updateInfo() {
//        MyDBHelper myHelper = new MyDBHelper(this, GlobalManager.DBNAEM, null, 1);
//        SQLiteDatabase sqlite = myHelper.getReadableDatabase();
//        ContentValues values = new ContentValues();
//        String name = mEdtName.getText().toString();
//        String sig = mEdtSig.getText().toString();
//
//        values.put("name", name);
//        values.put("signature", sig);
//        values.put("headpath", mImagePath);
//
//        GlobalManager.user.setSignature(sig);
//        GlobalManager.user.setNickname(name);
//        GlobalManager.user.setHeadimage(mImagePath);
//
//        sqlite.update("user", values, "num=?", new String[]{mNum});
//        values.clear();
//    }

    //                        -----------头像相关----------
    //打开头像编辑页面
    private void headImageEdit() {
        popWnd = new PopupWindow();
        View view = LayoutInflater.from(this).inflate(R.layout.popwnd_activity_edit_headopen, null);

        //设置点击事件
        Button btnCapture = view.findViewById(R.id.mine_edit_headimage_capture);
        Button btnFromPhoto = view.findViewById(R.id.mine_edit_headimage_fromphoto);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                popWnd.dismiss();
            }
        });
        btnFromPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotos();
                popWnd.dismiss();
            }
        });

        //设置透明度
        setTransparent(popWnd);

        //设置视图  设置宽度高度
        popWnd.setContentView(view);
        popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //显示相关
        popWnd.setFocusable(true);
        popWnd.setOutsideTouchable(true);//点击外面可被销毁
        popWnd.setAnimationStyle(R.style.PopupWindowAnimation);
        popWnd.showAtLocation(view
                , Gravity.BOTTOM, 0, 0);
    }

    private void setTransparent(PopupWindow popWnd) {
        final Window window = getWindow();
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.7f;//设置透明度
        window.setAttributes(lp);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //恢复此时的透明度
                lp.alpha = 1f;
                window.setAttributes(lp);
            }
        });
    }

    //打开相机 返回图片路径
    private void openCamera() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机
        String time = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String name = "Jpeg_" + time + ".jpg";

        File image = createImageFile(name);

        //拍照后原图回存入此路径下
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            mUri = Uri.fromFile(image);
        } else {
            /**
             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
             */
            mUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", image);
        }
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);//此时getData返回位null
        startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
    }

    private File createImageFile(String imageName) {
        String storagePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
//        Toast.makeText(this, storagePath, Toast.LENGTH_SHORT).show();
//        Log.i("mxy", storagePath);
        File storageDir = new File(storagePath);
        File imageFile = new File(storageDir, imageName);
        mImagePath = imageFile.getAbsolutePath();//保存图片的路径
        return imageFile;
    }

    //打开相册
    private void openPhotos() {
        Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoIntent, REQUEST_OPEN_PHOTOS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null) {
                //当没有设置takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);的时候
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");//根据data得到位图
                mIvHead.setImageBitmap(bitmap);
            } else {
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                    mIvHead.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_OPEN_PHOTOS && resultCode == RESULT_OK) {
            mUri = data.getData();
            mImagePath = getRealPathFromURI(mUri);
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                mIvHead.setImageBitmap(bitmap);
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
