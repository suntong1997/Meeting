package com.example.meeting.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.framework.base.BaseBackActivity;
import com.example.framework.helper.FileHelper;
import com.example.framework.manager.BmobManager;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.LogUtils;
import com.example.framework.view.DialogView;
import com.example.framework.view.LoadingView;
import com.example.meeting.R;

import java.io.File;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

public class FirstUploadActivity extends BaseBackActivity implements View.OnClickListener {

    private CircleImageView iv_photo;
    private EditText et_nickname;
    private Button btn_upload;
    private TextView tv_camera;
    private TextView tv_ablum;
    private TextView tv_cancel;

    private LoadingView mLoadingView;
    private DialogView mPhotoSelectView;


    public static void startActivity(Activity mActivity, int requestCode) {
        Intent intent = new Intent(mActivity, FirstUploadActivity.class);
        mActivity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_upload);

        initView();
    }

    private void initView() {
        initPhotoView();
        iv_photo = findViewById(R.id.iv_photo);
        et_nickname = findViewById(R.id.et_nickname);
        btn_upload = findViewById(R.id.btn_upload);

        iv_photo.setOnClickListener(this);
        btn_upload.setOnClickListener(this);

        btn_upload.setEnabled(false);

        et_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    btn_upload.setEnabled(uploadFile != null);
                } else {
                    btn_upload.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initPhotoView() {
        mPhotoSelectView = DialogManager.getInstance().initView(this, R.layout.dialog_select_photo, Gravity.BOTTOM);
        mLoadingView = new LoadingView(this);
        mLoadingView.setLoadingText("正在上传头像...");
        tv_camera = mPhotoSelectView.findViewById(R.id.tv_camera);
        tv_camera.setOnClickListener(this);
        tv_ablum = mPhotoSelectView.findViewById(R.id.tv_ablum);
        tv_ablum.setOnClickListener(this);
        tv_cancel = mPhotoSelectView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_camera:
                DialogManager.getInstance().hide(mPhotoSelectView);
                FileHelper.getInstance().toCamera(this);
                break;
            case R.id.tv_cancel:
                DialogManager.getInstance().hide(mPhotoSelectView);
                break;
            case R.id.iv_photo:
                DialogManager.getInstance().show(mPhotoSelectView);
                break;
            case R.id.btn_upload:
                uploadPhoto();
                break;
            case R.id.tv_ablum:
                FileHelper.getInstance().toAlbum(this);
                DialogManager.getInstance().hide(mPhotoSelectView);
                break;
        }
    }

    private void uploadPhoto() {
        String nickName = et_nickname.getText().toString().trim();
        BmobManager.getInstance().uploadFirstPhoto(nickName, uploadFile, new BmobManager.OnUploadPhotoListener() {
            @Override
            public void OnUpdateDone() {
                mLoadingView.hide();
                setResult(RESULT_OK);
                Toast.makeText(FirstUploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void OnUpdateFail(BmobException e) {
                mLoadingView.hide();
                Toast.makeText(FirstUploadActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mLoadingView.show();
    }

    private File uploadFile = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FileHelper.CAMERA_REQUEST_CODE) {
                uploadFile = FileHelper.getInstance().getTempFile();
            } else if (requestCode == FileHelper.ALBUM_REQUEST_CODE) {
                Uri uri = data.getData();
                if (uri != null) {
                    String path = FileHelper.getInstance().getRealPathFromURI(this, uri);
                    if (!TextUtils.isEmpty(path)) {
                        uploadFile = new File(path);
                    }
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    iv_photo.setImageBitmap(bitmap);
                }
            }
            if (uploadFile != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(uploadFile.getPath());
                iv_photo.setImageBitmap(bitmap);

                String nickName = et_nickname.getText().toString().trim();
                btn_upload.setEnabled(!TextUtils.isEmpty(nickName));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}