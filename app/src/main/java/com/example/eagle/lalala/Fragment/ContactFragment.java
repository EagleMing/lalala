package com.example.eagle.lalala.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
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

    private View layout, layout_head;
    private ListView lvContact;
    private SideBar indexBar;
    private TextView mDialogText;
    private WindowManager mWindowManager;

    private static final String serviceUrl = "http://119.29.166.177:8080/getFriends";
    private ProgressDialog progressDialog;
    private JSONArray friendsJson;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            switch (msg.what) {
                case 1:
                    Toast.makeText(getActivity(), "加载成功！", Toast.LENGTH_SHORT).show();
                    if (friendsJson.length() == 0) {
                        Toast.makeText(getActivity(), "朋友为空！", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("ContactFrag::friends:", friendsJson.toString());
                        makefriendslist(friendsJson);
                    }
                    break;
                case -1:
                    Toast.makeText(getActivity(), "加载失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getActivity(), "添加好友成功", Toast.LENGTH_SHORT).show();
                    break;
                case -4:
                    Toast.makeText(getActivity(), "添加好友失败", Toast.LENGTH_SHORT).show();
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
        layout_head = getActivity().getLayoutInflater().inflate(
                R.layout.layout_head_friend, null);
        lvContact.addHeaderView(layout_head);

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
//        lvContact.setAdapter(new ContactAdapter(getActivity(),
//                DatasUtil.getUsers()));
        new getContact().execute();
        lvContact.setAdapter(new ContactAdapter(getActivity(),DatasUtil.sFriendsPDMs));
    }

    private void setOnListener() {
        lvContact.setOnItemClickListener(this);
        layout_head.findViewById(R.id.layout_addfriend)
                .setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layout_addfriend:// 添加好友
                //     Toast.makeText(getActivity(),"fuck",Toast.LENGTH_SHORT).show();
                final EditText editText = new EditText(getActivity());
                new AlertDialog.Builder(getActivity()).setTitle("请输入")
                        //.setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        JSONObject object = new JSONObject();
                        try {
                            object.put("userID", MainActivity.userId);
                            object.put("emailAddr", editText.getText().toString());
                            object.put("relation", 0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("juanjuan", object.toString());
                        new addFriend().execute(object);

                    }
                })
                        .setNegativeButton("取消", null).show();
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        User user = DatasUtil.getUsers().get(arg2 - 1);
//        User user = DatasUtil.getUsers().get(arg2 );
        Toast.makeText(getActivity(), user.getName(), Toast.LENGTH_SHORT).show();


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

    private void makefriendslist(JSONArray array) { //制作好友列表
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                FriendPDM friendPDM = new FriendPDM();
                friendPDM.setUserID(object.getLong("userID"));
                friendPDM.setUserName(object.getString("userName"));
                friendPDM.setIcon(object.getString("icon"));
                friendPDM.setEmailAddr(object.getString("emailAddr"));
                friendPDM.setSignature(object.getString("signature"));

                DatasUtil.sFriendsPDMs.add(friendPDM);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
            HttpUtil.getJsonArrayByHttp(serviceUrl, object, new HttpCallbackListener() {
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


    //============================================================================
    //添加朋友关系
    private static final String serviceUrl_addfriend = "http://119.29.166.177:8080/createRelation";


    //这里需要传入userID 和 friendID  类型都为long。还有关系relation 好友关系为 0，
    // 方法：long friendID;
    //long userID;


    private class addFriend extends AsyncTask<JSONObject, Void, Void> {
        private String status;
        private String info;

        @Override
        protected Void doInBackground(JSONObject... params) {

            HttpUtil.getJsonArrayByHttp(serviceUrl_addfriend, params[0], new HttpCallbackListener() {
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

