package com.example.meeting.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.example.framework.bmob.IMUser;
import com.example.framework.manager.BmobManager;
import com.example.framework.utils.CommonUtils;
import com.example.framework.utils.LogUtils;
import com.example.meeting.R;
import com.example.meeting.adapter.AddFriendAdapter;
import com.example.meeting.model.AddFriendModel;

import org.xml.sax.helpers.XMLReaderAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ContactFirendActivity extends AppCompatActivity {

    private RecyclerView mContactView;
    private Map<String, String> mContactMap = new HashMap<>();
    private List<AddFriendModel> mList = new ArrayList<>();
    private AddFriendAdapter mAddFriendAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_firend);

        initView();
    }

    private void initView() {
        mContactView = (RecyclerView) findViewById(R.id.mContactView);
        mContactView.setLayoutManager(new LinearLayoutManager(this));
        mContactView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAddFriendAdapter = new AddFriendAdapter(this, mList);
        mContactView.setAdapter(mAddFriendAdapter);
        mAddFriendAdapter.setOnclickListener(new AddFriendAdapter.OnclickListener() {
            @Override
            public void OnClick(int position) {

            }
        });
        loadContact();

        loadUser();
    }

    private void loadUser() {
        if (mContactMap.size() > 0) {
            for (Map.Entry<String, String> entry : mContactMap.entrySet()) {
                BmobManager.getInstance().queryPhoneUser(entry.getValue(), new FindListener<IMUser>() {
                    @Override
                    public void done(List<IMUser> list, BmobException e) {
                        if (e == null) {
                            if (CommonUtils.isEmpty(list)) {
                                IMUser imUser = list.get(0);
                                addContent(imUser, entry.getKey(), entry.getValue());
                            }
                        }
                    }
                });
            }
        }
    }

    private void loadContact() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        String name;
        String phone;
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phone = phone.replace(" ", "").replace("-", "");
            mContactMap.put(name, phone);
        }
    }

    private void addContent(IMUser imUser, String name, String phone) {
        AddFriendModel model = new AddFriendModel();
        model.setType(AddFriendAdapter.TYPE_CONTENT);
        model.setUserId(imUser.getObjectId());
        model.setPhoto(imUser.getPhoto());
        model.setAge(imUser.getAge());
        model.setNickName(imUser.getNickName());
        model.setDesc(imUser.getDesc());
        model.setContact(true);
        model.setContactName(name);
        model.setContactPhone(phone);
        mList.add(model);
        mAddFriendAdapter.notifyDataSetChanged();
    }
}