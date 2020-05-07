package com.example.dayrecord.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dayrecord.Utils.GlobalManager;
import com.example.dayrecord.R;
import com.example.dayrecord.data.Sharings;
import com.example.dayrecord.data.User;
import com.example.dayrecord.server.InfoHub;

public class AddShareFragment extends Fragment {

    private View mView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_share,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;
        TextView imageChoose=view.findViewById(R.id.add_share_imagechoose);
        //打开相册选择图片  通过fragment的容器找到fragment
        imageChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotos();
            }
        });
    }
    //打开相册
    private void openPhotos() {
        Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(photoIntent, 1);
    }


    public void saveInfo(String time,String imagePath){
        EditText edtContent=mView.findViewById(R.id.add_share_content);
        EditText edtAuthor=mView.findViewById(R.id.add_share_author);
        String content=edtContent.getText().toString();
        String author=edtAuthor.getText().toString();

        User user=GlobalManager.user;

        //填充信息到缓存
        Sharings sharingThings=new Sharings(0,0,0, content,author,user.getId(),String.valueOf(time),
                user.getNickname(),imagePath,user.getHeadimage());
//        GlobalManager.sharingList.add(sharingThings);

        //将信息填充到数据库

        new InfoHub().addUserSharing(getActivity(),sharingThings);
    }
}
