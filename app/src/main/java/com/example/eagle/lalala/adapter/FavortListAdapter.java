package com.example.eagle.lalala.adapter;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import com.example.eagle.lalala.MyApplication;
import com.example.eagle.lalala.PDM.likesPDM;
import com.example.eagle.lalala.R;
import com.example.eagle.lalala.bean.FavortItem;
import com.example.eagle.lalala.spannable.CircleMovementMethod;
import com.example.eagle.lalala.spannable.NameClickable;
import com.example.eagle.lalala.widgets.FavortListView;

import java.util.List;

/**
 * @author yiw
 * @Description:
 * @date 16/1/2 18:51
 */
public class FavortListAdapter {

    private FavortListView mListView;
    private List<likesPDM> datas;

    public List<likesPDM> getDatas() {
        return datas;
    }

    public void setDatas(List<likesPDM> datas) {
        this.datas = datas;
    }

    @NonNull
    public void bindListView(FavortListView listview) {
        if (listview == null) {
            throw new IllegalArgumentException("FavortListView is null ....");
        }
        mListView = listview;
    }


    public int getCount() {
        if (datas != null && datas.size() > 0) {
            return datas.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        if (datas != null && datas.size() > position) {
            return datas.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public void notifyDataSetChanged() {
        if (mListView == null) {
            throw new NullPointerException("listview is null, please bindListView first...");
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (datas != null && datas.size() > 0) {
            //添加点赞图标
            builder.append(setImageSpan());
            //builder.append("  ");
            likesPDM item = null;
            for (int i = 0; i < datas.size(); i++) {
                item = datas.get(i);
                if (item != null) {
                    builder.append(setClickableSpan(item.getUserName(), i));
                    if (i != datas.size() - 1) {
                        builder.append(", ");
                    }
                }
            }
        }
        mListView.setText(builder);
        mListView.setMovementMethod(new CircleMovementMethod(R.color.name_selector_color));
    }

    @NonNull
    private SpannableString setClickableSpan(String textStr, int position) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new NameClickable(mListView.getSpanClickListener(), position), 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    private SpannableString setImageSpan() {
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new ImageSpan(MyApplication.getContext(), R.drawable.im_ic_dig_tips, DynamicDrawableSpan.ALIGN_BASELINE),
                0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return imgSpanText;
    }
}
