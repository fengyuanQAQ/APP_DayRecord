package com.example.dayrecord.server;

import com.example.dayrecord.data.Comment;
import com.example.dayrecord.data.Sharings;
import com.example.dayrecord.data.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface WebApi {

    @GET("/user/getUserById")
    Call<Boolean> userExist(@Query("id") String id);

    @GET("/user/getUser")
    Call<User> retrieveUser(@Query("id") String id, @Query("password") String password);

    @GET("/sharing/getAll")
    Call<List<Sharings>> getAllSharings();

    @GET("/comment/getComments")
    Call<List<Comment>> getSharingComments(@Query("sharingid") long sharingid);

    //提交评论
    @POST("/comment/insert")
    Call<Comment> addComment(@Body Comment comment);

    @POST("user/insertUser")
    Call<User> insertUser(@Body User user);

    @PUT("/user/updateUser")
    Call<User> updateUser(@Body User user);

    //获取用户的分享
    @GET("/sharing/getUserSharings")
    Call<List<Sharings>> getUserSharings(@Query("userid") String userid);

    //添加用户分享
    @POST("/sharing/insertSharing")
    Call<Sharings> addUserSharing(@Body Sharings sharings);

    //删除用户分享
    @DELETE("/sharing/deleteOne")
    Call<ResponseBody> deleteShaingByid(@Query("id") int id);

    //删除用户所有分享
    @DELETE("/sharing/deleteAll")
    Call<ResponseBody> deleteAllUserSharings(@Query("userid") String userid);

    //删除该分享的评论
    @DELETE("/comment/deleteBySharingId")
    Call<ResponseBody> deleteSharingComments(@Query("sharingid") int sharingid);

    //根据列表删除所有评论
    @DELETE("/comment/deleteByList")
    Call<ResponseBody> deleteByList(@Query("sharingids") List<Integer> list);

    //上传文件
    @Multipart
    @POST("/file/uploadHeadImage")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part part,
                                       @PartMap Map<String, RequestBody> map);

    //获取头像
    @GET("/file/getHeadImage")
    Call<ResponseBody> getHeadImage(@Query("userid") String userid);

    //上传背景图片
    @Multipart
    @POST("/file/uploadBgImage")
    Call<ResponseBody> uploadShaingImage(@Part MultipartBody.Part part,
                                         @PartMap Map<String, RequestBody> map);

    //获取背景图片
    @GET("/file/getBgImage")
    Call<ResponseBody> getBgImage(@Query("id")String id);

    //更新点赞数
    @PUT("sharing/updateSharing")
    Call<Sharings> updateSharings(@Body Sharings sharings);

    //更新评论数量
    @PUT("sharing/addComNum")
    Call<Sharings> addComNum(@Query("id")int id);

}