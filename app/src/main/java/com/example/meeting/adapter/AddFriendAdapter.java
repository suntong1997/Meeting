package com.example.meeting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.helper.GlideHelper;
import com.example.meeting.R;
import com.example.meeting.model.AddFriendModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_CONTENT = 1;

    private Context mContext;
    private List<AddFriendModel> mList;
    private LayoutInflater inflater;

    private OnclickListener onclickListener;

    public void setOnclickListener(OnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    public AddFriendAdapter(Context mContext, List<AddFriendModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            return new TitleViewHolder(inflater.inflate(R.layout.layout_search_title_item, null));
        } else {
            return new ContentViewHolder(inflater.inflate(R.layout.layout_search_user_item, null));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AddFriendModel model = mList.get(position);
        if (model.getType() == TYPE_TITLE) {
            ((TitleViewHolder) holder).tv_title.setText(model.getTitle());
        } else if (model.getType() == TYPE_CONTENT) {
            GlideHelper.loadUrl(mContext, model.getPhoto(), ((ContentViewHolder) holder).iv_photo);
            ((ContentViewHolder) holder).iv_sex.setImageResource(model.isSex() ? R.drawable.img_boy_icon : R.drawable.img_girl_icon);
            ((ContentViewHolder) holder).tv_nickname.setText(model.getNickName());
            ((ContentViewHolder) holder).tv_age.setText(model.getAge() + "岁");
            ((ContentViewHolder) holder).tv_desc.setText(model.getDesc());
            if (model.isContact()) {
                ((ContentViewHolder) holder).ll_contact_info.setVisibility(View.VISIBLE);
                ((ContentViewHolder) holder).tv_contact_name.setText(model.getContactName());
                ((ContentViewHolder) holder).tv_contact_phone.setText(model.getContactPhone());
            }
        }
        holder.itemView.setOnClickListener(view -> {
            if (onclickListener != null) {
                onclickListener.OnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView iv_photo;
        private ImageView iv_sex;
        private TextView tv_nickname;
        private TextView tv_age;
        private TextView tv_desc;

        private LinearLayout ll_contact_info;
        private TextView tv_contact_name;
        private TextView tv_contact_phone;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_photo = itemView.findViewById(R.id.iv_photo);
            iv_sex = itemView.findViewById(R.id.iv_sex);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);

            ll_contact_info = itemView.findViewById(R.id.ll_contact_info);
            tv_contact_name = itemView.findViewById(R.id.tv_contact_name);
            tv_contact_phone = itemView.findViewById(R.id.tv_contact_phone);
        }
    }

    public interface OnclickListener {
        void OnClick(int position);
    }
}
