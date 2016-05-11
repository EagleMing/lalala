package com.example.eagle.lalala.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eagle.lalala.Activity.ImagePagerActivity;
import com.example.eagle.lalala.Activity.MainActivity;
import com.example.eagle.lalala.MyApplication;
import com.example.eagle.lalala.PDM.MarksPDM;
import com.example.eagle.lalala.PDM.commentsPDM;
import com.example.eagle.lalala.PDM.likesPDM;
import com.example.eagle.lalala.R;
import com.example.eagle.lalala.bean.ActionItem;
import com.example.eagle.lalala.bean.CircleItem;
import com.example.eagle.lalala.bean.CommentConfig;
import com.example.eagle.lalala.bean.CommentItem;
import com.example.eagle.lalala.bean.FavortItem;
import com.example.eagle.lalala.mvp.presenter.CirclePresenter;
import com.example.eagle.lalala.spannable.ISpanClick;
import com.example.eagle.lalala.utils.DatasUtil;
import com.example.eagle.lalala.widgets.CircularImage;
import com.example.eagle.lalala.widgets.CommentListView;
import com.example.eagle.lalala.widgets.FavortListView;
import com.example.eagle.lalala.widgets.MultiImageView;
import com.example.eagle.lalala.widgets.SnsPopupWindow;
import com.example.eagle.lalala.widgets.dialog.CommentDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yiw
 * @ClassName: CircleAdapter
 * @Description: 圈子列表的adapter
 * @date 2015-12-28 上午09:37:23
 */
public class CircleAdapter extends BaseAdapter {
    private Context mContext;
    private CirclePresenter mPresenter;
    //private List<CircleItem> datas = new ArrayList<CircleItem>();

    public void setCirclePresenter(CirclePresenter presenter) {
        mPresenter = presenter;
    }

//    public List<CircleItem> getDatas() {
//        return datas;
//    }

//    public void setDatas(List<CircleItem> datas) {
//        if (datas != null) {
//            this.datas = datas;
//        }
//    }

    public CircleAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return DatasUtil.sMarksPDMs_public.size();
    }

    @Override
    public Object getItem(int position) {
        return DatasUtil.sMarksPDMs_public.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        System.out.println("CircleAdaptr getView----------" + position);

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.adapter_circle_item, null);

            holder.headIv = (CircularImage) convertView.findViewById(R.id.headIv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
            holder.digLine = convertView.findViewById(R.id.lin_dig);

            holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
            holder.locationTv = (TextView) convertView.findViewById(R.id.locationTv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
            holder.deleteBtn = (TextView) convertView.findViewById(R.id.deleteBtn);
            holder.snsBtn = (ImageView) convertView.findViewById(R.id.snsBtn);
            holder.favortListTv = (FavortListView) convertView.findViewById(R.id.favortListTv);

            holder.imageView = (ImageView)convertView.findViewById(R.id.imgInLv);
            holder.digCommentBody = (LinearLayout) convertView.findViewById(R.id.digCommentBody);

            holder.commentList = (CommentListView) convertView.findViewById(R.id.commentList);
            holder.commentAdapter = new CommentAdapter(mContext);
            holder.favortListAdapter = new FavortListAdapter();

            holder.favortListTv.setAdapter(holder.favortListAdapter);
            holder.commentList.setAdapter(holder.commentAdapter);

            holder.snsPopupWindow = new SnsPopupWindow(mContext);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MarksPDM circleItem = DatasUtil.sMarksPDMs_public.get(position);
        final long markId = circleItem.getMarkId();
        final Bitmap headImg = circleItem.getIcon();
        final Bitmap mainImg = circleItem.getPhoto();
        String content = circleItem.getContent();
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(circleItem.getCreateTime());
        final List<likesPDM> favortDatas = circleItem.getLikes();
        final List<commentsPDM> commentsDatas = circleItem.getComments();
        boolean hasFavort = circleItem.hasFavort();
        boolean hasComment = circleItem.hasComment();

        holder.headIv.setImageBitmap(headImg);
//        holder.imageView.setImageBitmap(mainImg);
//        ImageLoader.getInstance().displayImage(headImg, holder.headIv);
//        ImageLoader.getInstance().displayImage(mainImg, holder.imageView);
//        ImageLoader.getInstance().displayImage(DatasUtil.PHOTOS[(int)Math.random()*DatasUtil.PHOTOS.length], holder.imageView);
        ImageLoader.getInstance().displayImage(DatasUtil.PHOTOS[5], holder.imageView);

        holder.nameTv.setText(circleItem.getUserName());
        holder.timeTv.setText(createTime);
        holder.contentTv.setText(content);
        holder.locationTv.setText(circleItem.getPositionName());////这里改成显示地点名字
        holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

        if (MainActivity.userId == circleItem.getUserId()) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            holder.deleteBtn.setVisibility(View.GONE);
        }

        holder.deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                if (mPresenter != null) {
                    mPresenter.deleteCircle(markId);
                }
            }
        });

        if (hasFavort || hasComment) {
            if (hasFavort) {//处理点赞列表
//                holder.favortListTv.setSpanClickListener(new ISpanClick() {
//                    @Override
//                    public void onClick(int position) {
//                        String userName = favortDatas.get(position).getUser().getName();
//                        String userId = favortDatas.get(position).getUser().getId();
//                        Toast.makeText(MyApplication.getContext(), userName + " &id = " + userId, Toast.LENGTH_SHORT).show();
//                    }
//                }); //暂时不设置点击跳转事件
                holder.favortListAdapter.setDatas(favortDatas);
                holder.favortListAdapter.notifyDataSetChanged();
                holder.favortListTv.setVisibility(View.VISIBLE);
            } else {
                holder.favortListTv.setVisibility(View.GONE);
            }

            if (hasComment) {//处理评论列表
//                holder.commentList.setOnItemClick(new CommentListView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(int commentPosition) {
//                        CommentItem commentItem = commentsDatas.get(commentPosition);
//                        if (DatasUtil.curUser.getId().equals(commentItem.getUser().getId())) {//复制或者删除自己的评论
//
//                            CommentDialog dialog = new CommentDialog(mContext, mPresenter, commentItem, position);
//                            dialog.show();
//                        } else {//回复别人的评论
//                            if (mPresenter != null) {
//                                CommentConfig config = new CommentConfig();
//                                config.circlePosition = position;
//                                config.commentPosition = commentPosition;
//                                config.commentType = CommentConfig.Type.REPLY;
//                                config.replyUser = commentItem.getUser();
//                                mPresenter.showEditTextBody(config);
//                            }
//                        }
//                    }
//                });
                holder.commentList.setOnItemLongClick(new CommentListView.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(int commentPosition) {
                        //长按进行复制或者删除
                        commentsPDM commentItem = commentsDatas.get(commentPosition);
                        CommentDialog dialog = new CommentDialog(mContext, mPresenter, commentItem, position);
                        dialog.show();
                    }
                });
                holder.commentAdapter.setDatas(commentsDatas);
                holder.commentAdapter.notifyDataSetChanged();
                holder.commentList.setVisibility(View.VISIBLE);

            } else {

                holder.commentList.setVisibility(View.GONE);
            }
            holder.digCommentBody.setVisibility(View.VISIBLE);
        } else {
            holder.digCommentBody.setVisibility(View.GONE);
        }

        holder.digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);

        final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
        //判断是否已点赞
        long curUserFavortId = circleItem.getCurUserFavortId(MainActivity.userId);
        if (curUserFavortId != -1) {
            snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
        } else {
            snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
        }
        snsPopupWindow.update();
        snsPopupWindow.setmItemClickListener(new PopupItemClickListener(position, circleItem, curUserFavortId));
        holder.snsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出popupwindow
                snsPopupWindow.showPopupWindow(view);
            }
        });

        //holder.urlTipTv.setVisibility(View.GONE);
        final int width = holder.imageView.getMeasuredWidth();
        final int height =  holder.imageView.getMeasuredHeight();

//        holder.imageView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<String> temp = new ArrayList<String>();
//                temp.add(mainImg);
//                ImagePagerActivity.imageSize = new ImageSize(width,height);
//                ImagePagerActivity.startImagePagerActivity(mContext,temp , position);
//            }
//        });//先不设点击事件
        return convertView;
    }

    class ViewHolder {
        public CircularImage headIv;
        public TextView nameTv;
        /**
         * 动态的内容
         */
        public TextView contentTv;
        public TextView timeTv;
        public TextView locationTv;
        public TextView deleteBtn;
        public ImageView snsBtn;
        /**
         * 点赞列表
         */
        public FavortListView favortListTv;

        public LinearLayout digCommentBody;
        public View digLine;

        /**
         * 评论列表
         */
        public CommentListView commentList;
        /**
         * 图片
         */
        public ImageView imageView;
        // ===========================
        public FavortListAdapter favortListAdapter;
        //public CommentAdapter bbsAdapter;
        public CommentAdapter commentAdapter;
        public SnsPopupWindow snsPopupWindow;
    }

    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private long mFavorId;
        //动态在列表中的位置
        private int mCirclePosition;
        private long mLasttime = 0;
        private MarksPDM mCircleItem;

        public PopupItemClickListener(int circlePosition, MarksPDM circleItem, long favorId) {
            this.mFavorId = favorId;
            this.mCirclePosition = circlePosition;
            this.mCircleItem = circleItem;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if (mPresenter != null) {
                        if ("赞".equals(actionitem.mTitle.toString())) {
                            mPresenter.addFavort(mCirclePosition);
                        } else {//取消点赞
                            mPresenter.deleteFavort(mCirclePosition, mFavorId);
                        }
                    }
                    break;
                case 1://发布评论
                    if (mPresenter != null) {
                        CommentConfig config = new CommentConfig();
                        config.circlePosition = mCirclePosition;
                        config.commentType = CommentConfig.Type.PUBLIC;
                        mPresenter.showEditTextBody(config);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
