package com.example.eagle.lalala.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eagle.lalala.Activity.MainActivity;
import com.example.eagle.lalala.NetWork.HttpCallbackListener;
import com.example.eagle.lalala.NetWork.HttpUtil;
import com.example.eagle.lalala.PDM.FriendPDM;
import com.example.eagle.lalala.R;
import com.example.eagle.lalala.adapter.ContactAdapter;
import com.example.eagle.lalala.bean.User;
import com.example.eagle.lalala.utils.CommonUtils;
import com.example.eagle.lalala.utils.DatasUtil;
import com.example.eagle.lalala.widgets.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NameList;

import java.util.ArrayList;

/**
 * Created by eagle on 2016/4/26.好友列表界面
 */
public class ContactFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private View layout,layout_head;
    private ListView lvContact;
    private SideBar indexBar;
    private TextView mDialogText;
    private WindowManager mWindowManager;

    private static final String serviceUrl="http://119.29.166.177:8080/getFriends";
    private ProgressDialog progressDialog;
    private JSONArray friendsJson;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            switch (msg.what) {
                case 1:
                    Toast.makeText(getActivity(),"加载成功！",Toast.LENGTH_SHORT).show();
                    if (friendsJson == null) {
                        Toast.makeText(getActivity(), "朋友为空！", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("ContactFrag::friends:", friendsJson.toString());
                        makefriendslist(friendsJson);
                    }
                    break;
                case -1:
                    Toast.makeText(getActivity(),"加载失败！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layout == null) {
            layout = getActivity().getLayoutInflater().inflate(R.layout.frag_contacts,
                    null);
            mWindowManager = (WindowManager) getActivity()
                    .getSystemService(Context.WINDOW_SERVICE);
            initViews();
            initData();
            setOnListener();
        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        return layout;
    }

    private void initViews() {
        lvContact = (ListView) layout.findViewById(R.id.lvContact);

        mDialogText = (TextView) LayoutInflater.from(getActivity()).inflate(
                R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);

        indexBar = (SideBar) layout.findViewById(R.id.sideBar);
        indexBar.setListView(lvContact);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
        indexBar.setTextView(mDialogText);
//        layout_head = getActivity().getLayoutInflater().inflate(
//                R.layout.layout_head_friend, null);
//        lvContact.addHeaderView(layout_head);

    }

    @Override
    public void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        mWindowManager.removeView(mDialogText);
        super.onDestroy();
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        initData();
    }

    private void initData() {
                lvContact.setAdapter(new ContactAdapter(getActivity(),
                        DatasUtil.getUsers()));
    }

    private void setOnListener() {
        lvContact.setOnItemClickListener(this);
//        layout_head.findViewById(R.id.layout_addfriend)
//                .setOnClickListener(this);
//        layout_head.findViewById(R.id.layout_search).setOnClickListener(this);
//        layout_head.findViewById(R.id.layout_group).setOnClickListener(this);
//        layout_head.findViewById(R.id.layout_public).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.layout_addfriend:// 添加好友   暂时把这个按钮在xml里选择了不能点击，以后再加上这个功能
                //Utils.start_Activity(getActivity(), NewFriendsListActivity.class);
//                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        User user = DatasUtil.getUsers().get(arg2 - 1);
        User user = DatasUtil.getUsers().get(arg2 );
        Toast.makeText(getActivity(),user.getName(),Toast.LENGTH_SHORT).show();


//        if (user != null) {
//            Intent intent = new Intent(getActivity(), FriendMsgActivity.class);
//            intent.putExtra(Constants.NAME, user.getUserName());
//            intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
//            intent.putExtra(Constants.User_ID, user.getTelephone());
//            getActivity().startActivity(intent);
//            getActivity().overridePendingTransition(R.anim.push_left_in,
//                    R.anim.push_left_out);
//        }

    }

    private ArrayList<FriendPDM> makefriendslist(JSONArray array) { //制作好友列表
        ArrayList<FriendPDM> friendlist = new ArrayList<>();
        for(int i=0;i<array.length();i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                FriendPDM friendPDM = new FriendPDM();
                friendPDM.setUserID(object.getLong("userID"));
                friendPDM.setUserName(object.getString("userName"));
                friendPDM.setIcon(object.getString("icon"));
                friendPDM.setEmailAddr(object.getString("emailAddr"));
                friendPDM.setSignature(object.getString("signature"));

                friendlist.add(friendPDM);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return friendlist;
    }

    private class getContact extends AsyncTask<Void, Void, Void> {
        private String status;
        private String info;

        @Override
        protected Void doInBackground(Void... params) {

            JSONObject object = new JSONObject();
            try {
                object.put("userID", MainActivity.userId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpUtil.getJsonArrayByHttp(serviceUrl,object, new HttpCallbackListener() {
                @Override
                public void onFinishGetJson(JSONObject jsonObject) {
                    if (jsonObject == null) {
                        Log.i("status", "json:null");
                    } else if (jsonObject != null) {
                        try {
                            Log.i("status", "json:" + jsonObject.toString());
                            status = jsonObject.getString("status");
                            info = jsonObject.getString("info");
                            friendsJson = jsonObject.getJSONArray("friendsList");
                            Log.i("status1", "status:" + status + " info:" + info);
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
//                    Log.e("LoginFrag", e.getMessage());
                    status = "0";
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载,请稍候...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}

