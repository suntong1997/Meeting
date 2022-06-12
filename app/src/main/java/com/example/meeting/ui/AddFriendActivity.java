package com.example.meeting.ui;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.framework.base.BaseBackActivity;
import com.example.framework.bmob.IMUser;
import com.example.framework.manager.BmobManager;
import com.example.framework.utils.CommonUtils;
import com.example.meeting.R;
import com.example.meeting.adapter.AddFriendAdapter;
import com.example.framework.adapter.CommonAdapter;
import com.example.framework.adapter.CommonViewHolder;
import com.example.meeting.model.AddFriendModel;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddFriendActivity extends BaseBackActivity implements View.OnClickListener {

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_CONTENT = 1;

    private LinearLayout ll_to_contact;
    private EditText et_phone;
    private ImageView iv_search;
    private RecyclerView mSearchResultView;

    private CommonAdapter<AddFriendModel> mAdapter;
    private List<AddFriendModel> mList = new ArrayList<>();
    private View include_empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        initView();
    }

    private void initView() {
        include_empty_view = findViewById(R.id.include_empty_view);
        ll_to_contact = (LinearLayout) findViewById(R.id.ll_to_contact);
        et_phone = (EditText) findViewById(R.id.et_phone);
        iv_search = (ImageView) findViewById(R.id.iv_search);

        mSearchResultView = (RecyclerView) findViewById(R.id.mSearchResultView);

        ll_to_contact.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        mSearchResultView.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnMoreBindDataListener<AddFriendModel>() {
            @Override
            public int getItemType(int position) {
                return mList.get(position).getType();
            }

            @Override
            public void onBindViewHolder(AddFriendModel model, CommonViewHolder viewHolder, int type, int position) {
                if (type == TYPE_TITLE) {
                    viewHolder.setText(R.id.tv_title, model.getTitle());
                } else if (type == TYPE_CONTENT) {
                    //设置头像
                    viewHolder.setImageUrl(AddFriendActivity.this, R.id.iv_photo, model.getPhoto());
                    //设置性别
                    viewHolder.setImageResource(R.id.iv_sex,
                            model.isSex() ? R.drawable.img_boy_icon : R.drawable.img_girl_icon);
                    //设置昵称
                    viewHolder.setText(R.id.tv_nickname, model.getNickName());
                    //年龄
                    viewHolder.setText(R.id.tv_age, model.getAge() + getString(R.string.text_search_age));
                    //设置描述
                    viewHolder.setText(R.id.tv_desc, model.getDesc());
                }
            }

            @Override
            public int getLayoutId(int type) {
                if (type == TYPE_TITLE) {
                    return R.layout.layout_search_title_item;
                } else if (type == TYPE_CONTENT) {
                    return R.layout.layout_search_user_item;
                }
                return 0;
            }
        });

        mSearchResultView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                queryPhoneUser();
                break;
            case R.id.ll_to_contact:
                if (checkPermission(Manifest.permission.READ_CONTACTS)) {
                    startActivity(new Intent(this, ContactFirendActivity.class));
                } else {
                    requestPermission(new String[]{Manifest.permission.READ_CONTACTS});
                }
                break;
        }
    }

    private void queryPhoneUser() {
        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, getString(R.string.text_login_phone_null), Toast.LENGTH_SHORT).show();
            return;
        }

        String phoneNumber = BmobManager.getInstance().getUser().getMobilePhoneNumber();
        if (phoneNumber.endsWith(phone)) {
            return;
        }

        BmobManager.getInstance().queryPhoneUser(phone, new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                if (e != null) {
                    return;
                }
                if (CommonUtils.isEmpty(list)) {
                    IMUser imUser = list.get(0);
                    include_empty_view.setVisibility(View.GONE);
                    mSearchResultView.setVisibility(View.VISIBLE);

                    mList.clear();

                    addTitle("查询结果");
                    addContent(imUser);
                    mAdapter.notifyDataSetChanged();

                    pushUser();

                } else {
                    include_empty_view.setVisibility(View.VISIBLE);
                    mSearchResultView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void pushUser() {
        BmobManager.getInstance().queryAllUser(new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                if (e == null) {
                    if (CommonUtils.isEmpty(list)) {
                        addTitle("推荐好友");
                        int num = list.size() <= 100 ? list.size() : 100;
                        for (int i = 0; i < num; i++) {
                            String phoneNumber = BmobManager.getInstance().getUser().getMobilePhoneNumber();
                            if (list.get(i).getMobilePhoneNumber().equals(phoneNumber)) {
                                continue;
                            }
                            addContent(list.get(i));
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void addTitle(String title) {
        AddFriendModel model = new AddFriendModel();
        model.setType(AddFriendAdapter.TYPE_TITLE);
        model.setTitle(title);
        mList.add(model);
    }

    private void addContent(IMUser imUser) {
        AddFriendModel model = new AddFriendModel();
        model.setType(AddFriendAdapter.TYPE_CONTENT);
        model.setUserId(imUser.getObjectId());
        model.setPhoto(imUser.getPhoto());
        model.setAge(imUser.getAge());
        model.setNickName(imUser.getNickName());
        model.setDesc(imUser.getDesc());
        mList.add(model);
    }
}