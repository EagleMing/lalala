package com.example.eagle.lalala.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.example.eagle.lalala.PDM.FriendPDM;
import com.example.eagle.lalala.bean.User;
import com.example.eagle.lalala.utils.MyViewHolder;
import com.example.eagle.lalala.utils.PingYinUtil;
import com.example.eagle.lalala.utils.PinyinComparator;

import com.example.eagle.lalala.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;

import java.util.Collections;
import java.util.List;

public class ContactAdapter extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private List<FriendPDM> UserInfos;// 好友信息

	public ContactAdapter(Context mContext, List<FriendPDM> UserInfos) {
		this.mContext = mContext;
		this.UserInfos = UserInfos;
		// 排序(实现了中英文混排)
		Collections.sort(UserInfos, new PinyinComparator());
	}

	@Override
	public int getCount() {
		return UserInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return UserInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FriendPDM user = UserInfos.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.contact_item, null);

		}
		ImageView ivAvatar = MyViewHolder.get(convertView,
				R.id.contactitem_avatar_iv);
		TextView tvCatalog = MyViewHolder.get(convertView,
				R.id.contactitem_catalog);
		TextView tvNick = MyViewHolder.get(convertView, R.id.contactitem_nick);
		char catalog = PingYinUtil.converterToFirstSpell(user.getUserName()).substring(0,1).toUpperCase()
				.charAt(0);
		if (position == 0) {
			tvCatalog.setVisibility(View.VISIBLE);
			tvCatalog.setText(String.valueOf(catalog));
		} else {
			FriendPDM Nextuser = UserInfos.get(position - 1);
			char lastCatalog = PingYinUtil.converterToFirstSpell(
					Nextuser.getUserName()).substring(0,1).toUpperCase().charAt(0);
			if (catalog == lastCatalog) {
				tvCatalog.setVisibility(View.GONE);
			} else {
				tvCatalog.setVisibility(View.VISIBLE);
				tvCatalog.setText(String.valueOf(catalog));
			}
		}



		ivAvatar.setImageBitmap(user.getIcon());
		tvNick.setText(user.getUserName());
		return convertView;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < UserInfos.size(); i++) {
			FriendPDM user = UserInfos.get(i);
			String l = PingYinUtil.converterToFirstSpell(user.getUserName())
					.substring(0, 1);
			char firstChar = l.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}



	@Override
	public Object[] getSections() {
		return null;
	}
}
