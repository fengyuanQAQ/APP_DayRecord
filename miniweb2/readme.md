基本接口：http://101.200.230.4:9120

用户接口：/user

    getUserById:get 
        参数：String id 
        return user
    updateUser:put
        参数：User user
        return user
    insertUser:post
        参数：User user
        return user
     
评论接口:/comment
    
    getComments:获取对应分下那个的所有评论 get
        参数：int shringid
        return Lits<Comments> comments;   
    insert：添加评论 post
        参数：Comment comment
        return Comment
    delete:删除 delete
        参数：int id
        return 删除{id}为的评论
    deleteByList：批量删除 delete
        参数:Lits<int> ids
        return 删除成功
  
  分享接口：/sharing
  
    getUserSharings:获取用户分享 get
        参数：String uerid
        return List<Sharings> sharings
    getAll:获取所有用户分享 get
        参数：无
        return List<Sharings> sharings
    insertSharing：插入分享 post
        参数： int sharingid
        return Sharing sharing
    deleteAll:删除用户所有分享 delete
        参数：String userid
        return 删除成功
    deleteOne:删除单个分享 delete
        参数：int sharingid
        return 删除成功
    updateSharing:更新分享的评论数和点赞数 put
        参数：Sharing sharing
        return Sharirng
   
 文件接口：/file
    uploadHeadImage:上传头像 post
         参数：MultipartFile file，String userid
         return 文件保存的路径
    getHeadImage:返回头像 get
         参数：String userid
         return 文件流
        
     
        
    
