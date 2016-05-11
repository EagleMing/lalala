package com.example.eagle.lalala.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eagle.lalala.Activity.MainActivity;
import com.example.eagle.lalala.NetWork.HttpCallbackListener;
import com.example.eagle.lalala.NetWork.HttpUtil;
import com.example.eagle.lalala.PDM.BasicEnum.Authorities;
import com.example.eagle.lalala.PDM.MarksPDM;
import com.example.eagle.lalala.PDM.commentsPDM;
import com.example.eagle.lalala.PDM.likesPDM;
import com.example.eagle.lalala.R;
import com.example.eagle.lalala.adapter.CircleAdapter;
import com.example.eagle.lalala.bean.CircleItem;
import com.example.eagle.lalala.bean.CommentConfig;
import com.example.eagle.lalala.bean.CommentItem;
import com.example.eagle.lalala.bean.FavortItem;
import com.example.eagle.lalala.mvp.presenter.CirclePresenter;
import com.example.eagle.lalala.mvp.view.ICircleView;
import com.example.eagle.lalala.utils.CommonUtils;
import com.example.eagle.lalala.utils.DatasUtil;
import com.example.eagle.lalala.widgets.CommentListView;
import com.example.neilhy.pulltorefresh_lib.PtrDefaultHandler;
import com.example.neilhy.pulltorefresh_lib.PtrFrameLayout;
import com.example.neilhy.pulltorefresh_lib.PtrHandler;
import com.example.neilhy.pulltorefresh_lib.header.StoreHouseHeader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eagle on 2016/4/9.
 */
public class SharedFragment extends ListFragment implements ICircleView {

    private String serviceUrl;

    private String[] mStringList = {"WeMark Sun.", "WeMark Mon.", "WeMark Tues.", "WeMark Wed.", "WeMark Thur.", "WeMark Fri.", "WeMark Sat."};


    ListView mCircleLv;
    private CircleAdapter mAdapter;
    private int mScreenHeight;
    private int mEditTextBodyHeight;
    private int mCurrentKeyboardH;
    private int mSelectCircleItemH;
    private int mSelectCommentItemOffset;

    private CirclePresenter mPresenter;
    private CommentConfig mCommentConfig;
    private JSONArray MarksjsonArray;

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (MarksjsonArray != null) {//不知道用jsonArray去接受的话，会报错，先调试用jsonobject去接收，等我找出原因先。
                        makeMarksList(MarksjsonArray);
//                        Log.i("SharedFrag:Array::", MarksjsonArray.toString());
                        Toast.makeText(getActivity(), "刷新Marks成功", Toast.LENGTH_SHORT).show();
                        mCircleLv.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case -1:
                    Toast.makeText(getActivity(), "刷新Marks失败……", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(getActivity(), "评论失败……", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getActivity(), "点赞成功", Toast.LENGTH_SHORT).show();
                    break;
                case -3:
                    Toast.makeText(getActivity(), "点赞失败……", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getActivity(), "删除Mark成功", Toast.LENGTH_SHORT).show();
                    break;
                case -4:
                    Toast.makeText(getActivity(), "删除Mark失败……", Toast.LENGTH_SHORT).show();
                    break;
            }
            mPtrFrameLayout.refreshComplete();

        }
    };

    @Bind(R.id.btn_focused)
    TextView mBtnFocus;
    @Bind(R.id.circleEt)
    EditText mEditText;
    @Bind(R.id.sendIv)
    ImageView sendIv;
    @Bind(R.id.editTextBodyLl)
    LinearLayout mEditTextBody;
    private PtrFrameLayout mPtrFrameLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            serviceUrl = bundle.getString("url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_shared, container, false);
        ButterKnife.bind(this, v);
        mPresenter = new CirclePresenter(this);
        mCircleLv = (ListView) v.findViewById(android.R.id.list);
        mPtrFrameLayout = (PtrFrameLayout) v.findViewById(R.id.FrameInStrFrag);
        //loadData();
        mAdapter = new CircleAdapter(getActivity());
        mAdapter.setCirclePresenter(mPresenter);
        new FreshMarks().execute();
        initView();

        return v;
    }


    private void initView() {
        StoreHouseHeader header = new StoreHouseHeader(getActivity());
        header.setPadding(0, 25, 0, 0);
        header.initWithString(getWeekOfDay());//获得当前星期的字符串

        mPtrFrameLayout.setDurationToCloseHeader(2000);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);

        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(false);
            }
        }, 100);

        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mCircleLv, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
//                frame.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadData();
//                        frame.refreshComplete();
//                    }
//                }, 2000);
//                ptrFrameLayout = frame;
                new FreshMarks().execute();
            }
        });


        mCircleLv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mEditTextBody.getVisibility() == View.VISIBLE) {
                    //mEditTextBody.setVisibility(View.GONE);
                    //CommonUtils.hideSoftInput(MainActivity.this, mEditText);
                    updateEditTextBodyVisible(View.GONE, null);
                    return true;
                }
                return false;
            }
        });

//        mAdapter = new CircleAdapter(getActivity());
//        mAdapter.setCirclePresenter(mPresenter);
        // mCircleLv.setAdapter(mAdapter);
        sendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    //发布评论
                    String content = mEditText.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(getActivity(), "评论内容不能为空...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mPresenter.addComment(content, mCommentConfig);
                }
                updateEditTextBodyVisible(View.GONE, null);
            }
        });

        setViewTreeObserver();
    }

    private String getWeekOfDay() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int datOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (datOfWeek < 0) {
            datOfWeek = 0;
        }
        return mStringList[datOfWeek];
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_posted, R.id.btn_focused})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_posted:
                CommonUtils.changeFrag(getActivity(), "posted_frag");
                break;
            case R.id.btn_focused:
                CommonUtils.changeFrag(getActivity(), "focused_frag");
                break;
        }
    }

    @Override
    public void update2DeleteCircle(long markId) {
        for (int i = 0; i < DatasUtil.sMarksPDMs_public.size(); i++) {
            if (markId == DatasUtil.sMarksPDMs_public.get(i).getMarkId()) {
                DatasUtil.sMarksPDMs_public.remove(i);
                new deleteMark().execute(markId);
                return;
            }
        }
    }

    @Override
    public void update2AddFavorite(int circlePosition, likesPDM addItem) {
        if (addItem != null) {
            DatasUtil.sMarksPDMs_public.get(circlePosition).getLikes().add(addItem);
            new addLike().execute(MainActivity.userId, DatasUtil.sMarksPDMs_public.get(circlePosition).getMarkId());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void update2DeleteFavort(int circlePosition, long favortId) {
        List<likesPDM> items = DatasUtil.sMarksPDMs_public.get(circlePosition).getLikes();
        for (int i = 0; i < items.size(); i++) {
            if (favortId == items.get(i).getUserId()) {
                items.remove(i);
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void update2AddComment(int circlePosition, commentsPDM addItem) {
        if (addItem != null) {
            JSONObject object = new JSONObject();
            try {
                object.put("userID", addItem.getFriendId());
                object.put("markID", addItem.getMarkId());
                object.put("content", addItem.getContent());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new addComment().execute(object);
            DatasUtil.sMarksPDMs_public.get(circlePosition).getComments().add(addItem);
            mAdapter.notifyDataSetChanged();

        }
        //清空评论文本
        mEditText.setText("");
    }

    @Override
    public void update2DeleteComment(int circlePosition, long commentId) {
//        List<CommentItem> items = mAdapter.getDatas().get(circlePosition).getComments();
//        for (int i = 0; i < items.size(); i++) {
//            if (commentId.equals(items.get(i).getId())) {
//                items.remove(i);
//                mAdapter.notifyDataSetChanged();
//                return;
//            }
//        }
    }

    @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
        mCommentConfig = commentConfig;
        mEditTextBody.setVisibility(visibility);

        measureCircleItemHighAndCommentItemOffset(commentConfig);

        if (View.VISIBLE == visibility) {
            getActivity().findViewById(R.id.menu_button).setVisibility(View.GONE);
            mEditText.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(mEditText.getContext(), mEditText);


        } else if (View.GONE == visibility) {
            //隐藏键盘

            CommonUtils.hideSoftInput(mEditText.getContext(), mEditText);
            getActivity().findViewById(R.id.menu_button).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。

        int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH -
                mEditTextBodyHeight - getActivity().findViewById(R.id.toolbar).getHeight() - 50;
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + mSelectCommentItemOffset;
        }
        return listviewOffset;
    }

    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return;

        int headViewCount = mCircleLv.getHeaderViewsCount();
        int firstPosition = mCircleLv.getFirstVisiblePosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = mCircleLv.getChildAt(headViewCount + commentConfig.circlePosition - firstPosition);
        if (selectCircleItem != null) {
            mSelectCircleItemH = selectCircleItem.getHeight();
            if (headViewCount > 0 && firstPosition < headViewCount && commentConfig.circlePosition == 0) {
                //如果有headView，而且head是可见的，并且处理偏移的位置是第一条动态，则将显示的headView的高度合并到第一条动态上
                for (int i = firstPosition; i < headViewCount; i++) {
                    mSelectCircleItemH += mCircleLv.getChildAt(i).getHeight();
                }
            }
        }

        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    mSelectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            mSelectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }


    private void setViewTreeObserver() {

        final ViewTreeObserver swipeRefreshLayoutVTO = mPtrFrameLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                mPtrFrameLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = mPtrFrameLayout.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);

                if (keyboardH == mCurrentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }

                mCurrentKeyboardH = keyboardH;
                mScreenHeight = screenH;//应用屏幕的高度
                mEditTextBodyHeight = mEditTextBody.getHeight();

                //偏移listview
                if (mCircleLv != null && mCommentConfig != null) {
                    int index = mCommentConfig.circlePosition == 0 ? mCommentConfig.circlePosition : (mCommentConfig.circlePosition + mCircleLv.getHeaderViewsCount());
                    mCircleLv.setSelectionFromTop(index, getListviewOffset(mCommentConfig));
                }
            }
        });
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void loadData() {
        new FreshMarks().execute();
        mAdapter.notifyDataSetChanged();
    }


    private class FreshMarks extends AsyncTask<Void, Void, String> {
        private String status;
        private String info;

        @Override
        protected String doInBackground(Void... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("userID", MainActivity.userId);
                Log.i("SharedFrag:id:", object.get("userID").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpUtil.getJsonArrayByHttp(serviceUrl, object, new HttpCallbackListener() {
                @Override
                public void onFinishGetJson(JSONObject jsonObject) {
                    if (jsonObject != null) {
                        try {
                            status = jsonObject.getString("status");
                            info = jsonObject.getString("info");
                            MarksjsonArray = jsonObject.getJSONArray("marks");////////////////////////这里是object还是listobject
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Message message = new Message();
                    if (status.equals("1") && info.equals("OK")) {
                        message.what = 1;
                    } else {
                        message.what = -1;
                    }
                    handler.sendMessage(message);
                }

                @Override
                public void onFinishGetString(String response) {

                }

                @Override
                public void onError(Exception e) {
//                    Log.e("SharedFrag", e.getMessage());
                    status = "0";
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }

    private void makeMarksList(JSONArray jsonArray) {//创建朋友圈列表
        // List<MarksPDM> marksList = new ArrayList<>();
        DatasUtil.sMarksPDMs_public.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject marksObject = jsonArray.getJSONObject(i);
                Log.i("SharedFrag:" + i + ":obj:", marksObject.toString());

                MarksPDM marksPDM = new MarksPDM();
                marksPDM.setUserId(marksObject.getLong("userID"));
                marksPDM.setUserName(marksObject.getString("userName"));
                marksPDM.setIcon(marksObject.getString("icon"));
                marksPDM.setMarkId(marksObject.getLong("markID"));
                marksPDM.setPositionName(marksObject.getString("positionName"));
                marksPDM.setLongitude(marksObject.getDouble("longitude"));
                marksPDM.setLatitude(marksObject.getDouble("latitude"));
                marksPDM.setCreateTime(new Timestamp((long) marksObject.get("createTime")));
                marksPDM.setContent(marksObject.getString("content"));
                marksPDM.setPhoto(marksObject.getString("photo"));
                marksPDM.setAuthority(Authorities.values()[marksObject.getInt("authority")]);

                JSONArray commentsObject = marksObject.getJSONArray("comments");
                List<commentsPDM> commentsList = new ArrayList<>();
                for (int j = 0; j < commentsObject.length(); j++) {
                    JSONObject comment = commentsObject.getJSONObject(j);
                    Log.i("SharedFrag:" + i + ":com:", comment.toString());

                    commentsPDM commentsPDM = new commentsPDM();
                    commentsPDM.setCommentId(comment.getLong("commentId"));
                    commentsPDM.setMarkId(comment.getLong("markID"));
                    commentsPDM.setFriendId(comment.getLong("friendID"));
                    commentsPDM.setFriendName(comment.getString("friendName"));
                    commentsPDM.setContent(comment.getString("content"));
                    commentsPDM.setCommentTime(new Timestamp((long) comment.get("commentTime")));
                    commentsList.add(commentsPDM);
                }

                JSONArray likesObject = marksObject.getJSONArray("likes");
                List<likesPDM> likesList = new ArrayList<>();
                for (int k = 0; k < likesObject.length(); k++) {
                    JSONObject like = likesObject.getJSONObject(k);
                    Log.i("SharedFrag:" + i + ":like:", like.toString());

                    likesPDM likesPDM = new likesPDM();
                    likesPDM.setLikeId(like.getLong("likeID"));
                    likesPDM.setUserId(like.getLong("friendID"));
                    likesPDM.setMarkId(like.getLong("markID"));
                    likesPDM.setUserName(like.getString("friendName"));//服务器那边没有给这个值。。。要协商
                    likesList.add(likesPDM);
                }
                marksPDM.setComments(commentsList);
                marksPDM.setLikes(likesList);

                DatasUtil.sMarksPDMs_public.add(marksPDM);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




    //======================================================================================
    //添加评论的请求
    private static final String serviceUrl_addcomment = "http://119.29.166.177:8080/addComment";

    //    //这里需要传入userID 和 markID 还有String 评论，类型都为long。
//    // 方法：long markID;
//    //long userID;
//    //String content;
//    JSONObject object = new JSONObject();
//    object.put("userID", userID);
//    object.put("markID", markID);
//    object.put("content",content);

//    //new addComment().execute(object);//调用这个异步类的时候，直接把上面的jsonobject传入即可

    private class addComment extends AsyncTask<JSONObject, Void, Void> {
        private String status;
        private String info;

        @Override
        protected Void doInBackground(JSONObject... params) {

            HttpUtil.getJsonArrayByHttp(serviceUrl_addcomment, params[0], new HttpCallbackListener() {
                @Override
                public void onFinishGetJson(JSONObject jsonObject) {
                    if (jsonObject == null) {
                        Log.i("status", "json:null");
                    } else if (jsonObject != null) {
                        try {
                            status = jsonObject.getString("status");
                            info = jsonObject.getString("info");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Message message = new Message();
                    if (status.equals("1") && info.equals("OK")) {
                        message.what = 2;
                    } else {
                        message.what = -2;
                    }
                    handler.sendMessage(message);
                }

                @Override
                public void onFinishGetString(String response) {

                }

                @Override
                public void onError(Exception e) {
                    Log.e("LoginFrag", e.getMessage());
                    status = "0";
                }
            });
            return null;
        }
    }


    //=======================================================================================
    //添加赞的请求
    private static final String addlike_serviceUrl = "http://119.29.166.177:8080/addLike";

    //这里需要传入userID 和 markID，类型都为long。
    // 方法：long markID;
    //long userID;
    //new addLike().execute(userID, markID);
    private class addLike extends AsyncTask<Long, Void, Void> {
        private String status;
        private String info;

        @Override
        protected Void doInBackground(Long... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("userID", params[0]);//////记得参数的顺序不要弄乱了，第一个为userID，第二个为markID
                object.put("markID", params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpUtil.getJsonArrayByHttp(addlike_serviceUrl, object, new HttpCallbackListener() {
                @Override
                public void onFinishGetJson(JSONObject jsonObject) {
                    if (jsonObject == null) {
                        Log.i("status", "json:null");
                    } else if (jsonObject != null) {
                        try {
                            status = jsonObject.getString("status");
                            info = jsonObject.getString("info");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Message message = new Message();
                    if (status.equals("1") && info.equals("OK")) {
                        message.what = 3;
                    } else {
                        message.what = -3;
                    }
                    handler.sendMessage(message);
                }

                @Override
                public void onFinishGetString(String response) {

                }

                @Override
                public void onError(Exception e) {
                    Log.e("LoginFrag", e.getMessage());
                    status = "0";
                }
            });
            return null;
        }
    }

    //======================================================================================
    //删除朋友圈的一条
    private static final String serviceUrl_deleteMark = "http://119.29.166.177:8080/deleteMark";

    //    //这里需要传入markID，类型为long。 方法：new deleteMark().execute(markId);
    private class deleteMark extends AsyncTask<Long, Void, Void> {
        private String status;
        private String info;

        @Override
        protected Void doInBackground(Long... params) {
            JSONObject object = new JSONObject();
            try {
                object.put("markID", params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpUtil.getJsonArrayByHttp(serviceUrl_deleteMark, object, new HttpCallbackListener() {
                @Override
                public void onFinishGetJson(JSONObject jsonObject) {
                    if (jsonObject == null) {
                        Log.i("status", "json:null");
                    } else if (jsonObject != null) {
                        try {
                            status = jsonObject.getString("status");
                            info = jsonObject.getString("info");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Message message = new Message();
                    if (status.equals("1") && info.equals("OK")) {
                        message.what = 4;
                    } else {
                        message.what = -4;
                    }
                    handler.sendMessage(message);
                }

                @Override
                public void onFinishGetString(String response) {

                }

                @Override
                public void onError(Exception e) {
                    Log.e("LoginFrag", e.getMessage());
                    status = "0";
                }
            });
            return null;
        }
    }
}
