package com.example.dayrecord.server;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dayrecord.R;
import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.Utils.MyDBHelper;
import com.example.dayrecord.activity.HomeActivity;
import com.example.dayrecord.adapter.CommentAdapter;
import com.example.dayrecord.adapter.DiscoverAdapter;
import com.example.dayrecord.data.Comment;
import com.example.dayrecord.data.Sharings;
import com.example.dayrecord.data.User;
import com.example.dayrecord.fragment.DiscoverFragment;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class InfoHub {
    private static final String TAG = "-----InfoHub-----";

    /*
     *登陆检验操作
     * */
    public void loginInfoMatch(Activity activity, String userid, String password, Button btnLogin) {
//        Log.d(TAG, userid+" "+password);

        //建立并且发送 请求
        Call<User> userCall = RetrofitHub.getWebApi().retrieveUser(userid, password);

        //异步连接
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int code = response.code();
                btnLogin.setClickable(false);//不在允许点击
                if (code == 200) {
                    User user = response.body();
                    if (user != null) {
                        //登陆 设置登陆的参数
                        //登陆界面的一些信息初始化
                        SharedPreferences mSp = activity.getSharedPreferences("loginData", MODE_PRIVATE);
                        SharedPreferences.Editor mEdit = mSp.edit();
                        if (!mSp.getBoolean("HavingLogin", false)) {
                            //第一次登陆
                            mEdit.putBoolean("HavingLogin", true);//设置标志
                        }
                        mEdit.putString("num", userid);
                        mEdit.putString("pass", password);
                        CheckBox cbRemPass=activity.findViewById(R.id.login_remPass);
                        CheckBox cbAuto=activity.findViewById(R.id.login_autoLogin);
                        boolean isChecked_pass = cbRemPass.isChecked();
                        boolean isChecked_auto = cbAuto.isChecked();

                        mEdit.putBoolean("rememberPassword", isChecked_pass);
                        mEdit.putBoolean("autoLogin", isChecked_auto);
                        mEdit.apply();

                        //跳转界面，填充用户信息,拉取用户的分享
                        getUserSharings(user.getId(),activity);

                        //获取用户头像
                        getHeadImage(activity,userid);

                        GlobalManager.user=user;
                        GlobalManager.loadDaily(user.getId());

                        //加载图像缓存
                        activity.startActivity(new Intent(activity, HomeActivity.class));
                        activity.finish();
                        Toast.makeText(activity,"欢迎您,"+GlobalManager.user.getNickname(),Toast.LENGTH_SHORT).show();
                    }
                }
                //允许点击
                btnLogin.setClickable(true);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(activity, "登陆失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*
     *获取所有用户的分享并且加载
     * */
    public void loadSharings(DiscoverFragment discoverFragment, RecyclerView recyclerView) {
        WebApi webApi = RetrofitHub.getWebApi();
        Call<List<Sharings>> call = webApi.getAllSharings();
        call.enqueue(new Callback<List<Sharings>>() {
            @Override
            public void onResponse(Call<List<Sharings>> call, Response<List<Sharings>> response) {
                int code = response.code();
                if (code == 200) {
                    List<Sharings> body = response.body();
                    if (body != null) {
                        Collections.reverse(body);
                        recyclerView.setAdapter(new DiscoverAdapter(discoverFragment,body));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Sharings>> call, Throwable t) {
                Toast.makeText(discoverFragment.getActivity(), "连接网络失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     *获取该分享的评论
     * */
    public void loadComment(Activity activity, long id, final RecyclerView recycler) {
        WebApi webApi = RetrofitHub.getWebApi();
        Call<List<Comment>> call = webApi.getSharingComments(id);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    List<Comment> body = response.body();
                    if (body != null) {
                        //加载头像
                        recycler.setAdapter(new CommentAdapter(activity, body));
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }

    /*
     * 检验注册操作
     * */
    public void registerRel(View v, String id, String pass, String nickname, AlertDialog dialog){
        Call<Boolean> call = RetrofitHub.getWebApi().userExist(id);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                int code = response.code();
//                Log.d(TAG, String.valueOf(code));;
                if (code== HttpURLConnection.HTTP_OK) {
                    Boolean body = response.body();
                    Activity activity= (Activity) v.getContext();
                    if (body) {
                        //账号存在 提示用户
                        Toast.makeText(activity, "该账号已经被注册", Toast.LENGTH_SHORT).show();
                        CheckBox cbNum = v.findViewById(R.id.login_register_num_reflect);
                        cbNum.setChecked(false);
                    }else {
                        //检验输入信息是否匹配
                        CheckBox cbName = v.findViewById(R.id.login_register_name_reflect);
                        CheckBox cbNum = v.findViewById(R.id.login_register_num_reflect);
                        CheckBox cbPass = v.findViewById(R.id.login_register_pass_reflect);
                        CheckBox cbSecPass = v.findViewById(R.id.login_register_secondpass_reflect);
                        if(cbName.isChecked()&&cbNum.isChecked()&&cbPass.isChecked()&&cbSecPass.isChecked()){
                            //将用户账号密码给登陆界面
                            EditText edtNum=activity.findViewById(R.id.login_user);
                            EditText edtPass=activity.findViewById(R.id.login_pass);
                            edtNum.setText(id);
                            edtPass.setText(pass);

                            User user=new User(id,pass,nickname,"无","");
                            //数据库添加账号
                            insertUser(activity,user);

                            //销毁该窗口
                            dialog.dismiss();
                        }else
                            Toast.makeText(activity, "输入信息错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(v.getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
    *数据库添加账号
    * */
    private void insertUser(Activity activity,User user){
        Call<User> call = RetrofitHub.getWebApi().insertUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK) {
                    User body = response.body();
                    if (body != null) {
//                        Log.d(TAG, body.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(activity, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 更新用户信息
     */
    public void updateUser(Activity activity,User user){
        Call<User> userCall = RetrofitHub.getWebApi().updateUser(user);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int code = response.code();
                if (code==HttpURLConnection.HTTP_OK) {
                    GlobalManager.user=user;
                }
                activity.finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(activity, "更新失败", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });
    }

    /**
     * 获取用户的分享
     */
    public void getUserSharings(String userid,Activity activity){
        Call<List<Sharings>> userSharings = RetrofitHub.getWebApi().getUserSharings(userid);
        userSharings.enqueue(new Callback<List<Sharings>>() {
            @Override
            public void onResponse(Call<List<Sharings>> call, Response<List<Sharings>> response) {
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK) {
                    List<Sharings> body = response.body();
                    if (body != null) {
                        GlobalManager.sharingList=body;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Sharings>> call, Throwable t) {
                Toast.makeText(activity, "网络获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 添加用户分享
     */
    public void addUserSharing(Activity activity,Sharings sharings){
        Call<Sharings> sharingsCall = RetrofitHub.getWebApi().addUserSharing(sharings);
        sharingsCall.enqueue(new Callback<Sharings>() {
            @Override
            public void onResponse(Call<Sharings> call, Response<Sharings> response) {
                int code = response.code();
                Log.d(TAG, String.valueOf(code));
                if (code== HttpURLConnection.HTTP_OK) {
                    Sharings body = response.body();
                    if (body != null) {
                        //将该分享加入全局缓存
                        GlobalManager.sharingList.add(body);

                        //加载图片
                        int id=body.getId();
                        String image = body.getImage();
                        //如果添加了图片
                        if (!"".equals(image)) {
                            //上传图片
                            uploadBg(activity,new File(image), String.valueOf(id));
                        }
                    }
                }
                activity.finish();
            }

            @Override
            public void onFailure(Call<Sharings> call, Throwable t) {
                Toast.makeText(activity, "连接网络失败", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });
    }

    /**
     * 提交评论
     */
    public void addComment(Activity activity, Comment comment, RecyclerView recyclerView){
        Call<Comment> commentCall = RetrofitHub.getWebApi().addComment(comment);
        commentCall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK) {
                    Comment body = response.body();
                    //该分享的评论数量+1
                    addComNum(activity,comment.getSharingid());
                    //重新加载评论
                    loadComment(activity,comment.getSharingid(),recyclerView);
                    if (body != null) {
//                        Log.d(TAG, body.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(activity, "连接网络失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 删除当前用户分享
     */
    public void deleteOneUserSharings(Activity activity, int id){
        Call<ResponseBody> stringCall = RetrofitHub.getWebApi().deleteShaingByid(id);
        stringCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK) {
                    //删除该记录的所有评论
                    deleteSharingCommentsById(activity,id);

                    //删除本地数据库的点赞记录
                    MyDBHelper myHelper = new MyDBHelper(activity, GlobalManager.DBNAEM, null, 1);
                    SQLiteDatabase sqlite = myHelper.getReadableDatabase();
//                    SqliteUtils.printDB(sqlite);
                    deleteStarMark(sqlite,id);
//                    Log.d(TAG, "--------");
//                    SqliteUtils.printDB(sqlite);
                    sqlite.close();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "网络连接失败，没有真正删除", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteStarMark(SQLiteDatabase seqlite,int id){
        seqlite.delete("sharing_stars", "mark=?", new String[]{String.valueOf(id)});
    }

    /**
     * 删除当前用户所有分享
     */
    public void deleteAllUserShaings(Activity activity,String userid){
        Call<ResponseBody> responseBodyCall = RetrofitHub.getWebApi().deleteAllUserSharings(userid);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK) {
                    //清除对应的评论
//                    deleteCommentsByList(activity,list);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "联网失败，没有真正删除", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 删除该分享对应的评论
     */
    public void deleteSharingCommentsById(Activity activity,int id){
        Call<ResponseBody> responseBodyCall = RetrofitHub.getWebApi().deleteSharingComments(id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "连接网络失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 根据列表删除对应的评论 用于清空所有分享
     */
    public void deleteCommentsByList(Activity activity,List<Integer> list){
//        Log.d(TAG, "11111");
        Call<ResponseBody> responseBodyCall = RetrofitHub.getWebApi().deleteByList(list);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
//                Log.d(TAG, String.valueOf(code));
                if (code== HttpURLConnection.HTTP_OK) {
                    //删除本地数据库的点赞记录
                    MyDBHelper myHelper = new MyDBHelper(activity, GlobalManager.DBNAEM, null, 1);
                    SQLiteDatabase sqlite = myHelper.getReadableDatabase();
//                    SqliteUtils.printDB(sqlite);
                    for (Integer integer : list) {
                        deleteStarMark(sqlite,integer);
                    }
//                    SqliteUtils.printDB(sqlite);
                    sqlite.close();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "连接网络失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 上传图片
     */
    public void uploadImage(Activity activity, File image, User user){
        Map<String,RequestBody> map=new HashMap<>();
        map.put("userid",RequestBody.create(MediaType.parse("text/plain")
                ,user.getId()));
        String imageName=image.getName();
        Log.d(TAG,imageName);
//        String imageType=imageName.substring(imageName.indexOf(".")+1);
        MediaType mediaType=MediaType.parse("image/*");
        RequestBody requestBody=RequestBody.create(mediaType,image);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file",imageName,requestBody);
        Call<ResponseBody> responseBodyCall = RetrofitHub.getWebApi().uploadImage(part, map);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
//                Log.d(TAG, String.valueOf(code));
                if (code== HttpURLConnection.HTTP_OK) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        try {
                            String imagePath = body.string();
//                            Log.d(TAG, imagePath);
                            //更新用户信息
                            GlobalManager.user=user;
                            GlobalManager.user.setHeadimage(imagePath);
                            updateUser(activity,GlobalManager.user);

                            //更新头像缓存
                            GlobalManager.headBytes=GlobalManager.fileToBytes(image);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "连接网路失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取用户的头像
     */
    public void getHeadImage(Activity activity, String userid){
        Call<ResponseBody> fileCall = RetrofitHub.getWebApi().getHeadImage(userid);

        fileCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
//                Log.d(TAG, String.valueOf(code));
                if (code== HttpURLConnection.HTTP_OK) {
                    ResponseBody body = response.body();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            byte[] bytes;
                            try {
                                bytes = body.bytes();
                                //加载头像缓存
                                GlobalManager.headBytes=bytes;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "连接网络失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取别人的头像
     */
    public void getSharingHeadImage(Activity activity, String userid,ImageView imageView){
        Call<ResponseBody> fileCall = RetrofitHub.getWebApi().getHeadImage(userid);

        fileCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
//                Log.d(TAG, String.valueOf(code));
                if (code== HttpURLConnection.HTTP_OK) {
                    ResponseBody body = response.body();
                    try {
                        byte[] bytes = body.bytes();
                        Glide.with(activity).load(bytes).into(imageView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "连接网络失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 上传背景图片
     */
    public void uploadBg(Activity activity,File image,String sharingid){
        Map<String,RequestBody> map=new HashMap<>();
        map.put("id",RequestBody.create(MediaType.parse("text/plain")
                ,sharingid));
        String imageName=image.getName();
        Log.d(TAG,imageName);
//        String imageType=imageName.substring(imageName.indexOf(".")+1);
        MediaType mediaType=MediaType.parse("image/*");
        RequestBody requestBody=RequestBody.create(mediaType,image);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file",imageName,requestBody);
        Call<ResponseBody> responseBodyCall = RetrofitHub.getWebApi().uploadShaingImage(part,
                map);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                Log.d(TAG, String.valueOf(code));
                if (code== HttpURLConnection.HTTP_OK) {
//                    ResponseBody body = response.body();
//                    try {
//                        String string = body.string();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "连接网络失败");
            }
        });
    }

    /**
     * 获取背景图片
     */
    public void getBgImage(Activity activity, Sharings sharings, ImageView imageView){
        Call<ResponseBody> bgImage = RetrofitHub.getWebApi().getBgImage(String.valueOf(sharings.getId()));
        bgImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
//                Log.d(TAG, String.valueOf(code)+"getbg");
                if (code== HttpURLConnection.HTTP_OK) {
                    try {
                        byte[] bytes = response.body().bytes();
                        Glide.with(activity).load(bytes).into(imageView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "网络加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 更新分享
     */
    public void updateShaings(Activity activity, Sharings sharings){
        Call<Sharings> sharingsCall = RetrofitHub.getWebApi().updateSharings(sharings);

        sharingsCall.enqueue(new Callback<Sharings>() {
            @Override
            public void onResponse(Call<Sharings> call, Response<Sharings> response) {
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Sharings> call, Throwable t) {
                Toast.makeText(activity, "连接网络失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 更新评论的数量
     */
    public void addComNum(Activity activity,int id){
        Call<Sharings> sharingsCall = RetrofitHub.getWebApi().addComNum(id);
        
        sharingsCall.enqueue(new Callback<Sharings>() {
            @Override
            public void onResponse(Call<Sharings> call, Response<Sharings> response) {
            }

            @Override
            public void onFailure(Call<Sharings> call, Throwable t) {
                Toast.makeText(activity, "连接网络失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}




