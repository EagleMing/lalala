package com.example.eagle.lalala.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eagle.lalala.R;

import com.example.eagle.lalala.NetWork.HttpCallbackListener;
import com.example.eagle.lalala.NetWork.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eagle on 2016/4/9.
 */
public class SignUpFragment extends Fragment {

    private static final String serviceUrl="http://119.29.166.177:8080/signUp";
    private ProgressDialog progressDialog;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getActivity(),"注册成功！",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    break;
                case -1:
                    Toast.makeText(getActivity(),"注册失败！请重新注册~",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Bind(R.id.input_nickname)
    EditText mInputNickname;
    @Bind(R.id.input_signup_email)
    EditText mInputSignupEmail;
    @Bind(R.id.input_signup_password)
    EditText mInputSignupPassword;
    @Bind(R.id.input_signup_repeatPassword)
    EditText mInputSignupRepeatPassword;
    @Bind(R.id.btn_signup)
    AppCompatButton mBtnSignup;
    @Bind(R.id.link_login)
    TextView mLinkLogin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_signup, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_signup, R.id.link_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                if(validate()) {
                    getActivity().setResult(Activity.RESULT_OK);
                    JSONObject object=new JSONObject();
                    try {
                        object.put("emailAddr",mInputSignupEmail.getText().toString());
                        object.put("userName", mInputNickname.getText().toString());
                        object.put("password", mInputSignupPassword.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new SignUp().execute(object);
//                    Log.i("status4", "status:" + status + " info:" + info);

                }
                break;
            case R.id.link_login:
//                    getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
                break;
        }
    }

    public boolean validate() {
        boolean valid = true;

        String name = mInputNickname.getText().toString();
        String email = mInputSignupEmail.getText().toString();
        String password = mInputSignupPassword.getText().toString();
        String repeatPassword = mInputSignupRepeatPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3 || name.length() > 10) {
            mInputNickname.setError("请输入长度为3到10的昵称");
            valid = false;
        } else {
            mInputNickname.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mInputSignupEmail.setError("请输入有效邮箱");
            valid = false;
        } else {
            mInputSignupEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 18) {
            mInputSignupPassword.setError("请输入6到18位长的密码");
            valid = false;
        } else {
            mInputSignupPassword.setError(null);
        }

        if(!password.equals(repeatPassword)){
            valid = false;
            Toast.makeText(getActivity(),"两次输入密码不一致",Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    private class SignUp extends AsyncTask<JSONObject, Void, String> {

        private String status;
        private String info;

        @Override
        protected String doInBackground(JSONObject... params) {

            HttpUtil.getJsonArrayByHttp(serviceUrl, params[0], new HttpCallbackListener() {
                @Override
                public void onFinishGetJson(JSONObject jsonObject) {
                    if (jsonObject == null) {
                        Log.i("status", "json:null" );
                    } else if(jsonObject != null){
                        try {
                            Log.i("status", "json:"+jsonObject.toString() );
                            status =jsonObject.getString("status");
                            info = jsonObject.getString("info");
                            Log.i("status1", "status:" + status + " info:" + info);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Message message=new Message();
                    if (status.equals("1") && info.equals("OK")) {
                        message.what=1;
                    }else{
                        message.what=-1;
                    }
                    handler.sendMessage(message);
                }

                @Override
                public void onFinishGetString(String response) {

                }

                @Override
                public void onError(Exception e) {
//                    Log.e("LoginFrag", e.getMessage());
                    status="0";
                }
            });
//            Log.i("status2", "status:" + status + " info:" + info);
//            if (status != null && info != null) {
//                if (status.equals("1") && info.equals("OK")) {
//                    return "ok";
//                }
//            }
//            return "no";
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在注册,请稍候...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Log.i("status3", "status:" + status + " info:" + info);
//            if (status.equals("1") && info.equals("OK")) {
//                Toast.makeText(getActivity(),"注册成功！",Toast.LENGTH_SHORT).show();
////                getActivity().finish();
//            }else {
//                Toast.makeText(getActivity(),"注册失败！请重新注册~",Toast.LENGTH_SHORT).show();
//            }
        }
    }

}
