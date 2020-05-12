package com.stx.xhb.DCAPlatform.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stx.xhb.DCAPlatform.R;
import com.stx.xhb.DCAPlatform.base.BaseApplication;
import com.stx.xhb.DCAPlatform.utils.API;
import com.stx.xhb.DCAPlatform.utils.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private EditText TeamName,Captain;
    private String EventId,UserId;
    private BaseApplication baseApplication;
    Toolbar settingToolbar;
    int index=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        EventId=intent.getStringExtra("EventId");
        setContentView(R.layout.fragment_sign_up);
        initWindow();
        ButterKnife.bind(this);
        initView();
        setListener();


    }
    //初始化控件
    private void initView() {
        settingToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(settingToolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        //设置显示返回上一级的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置标题
        getSupportActionBar().setTitle("报名");
        //设置标题栏字体颜色
        settingToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        settingToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        linearLayout=(LinearLayout)findViewById(R.id.fs_linear);
        TeamName=(EditText)findViewById(R.id.team_name);
        Captain=(EditText)findViewById(R.id.captain_name);
        baseApplication=(BaseApplication)getApplication();
        UserId=baseApplication.getUserId();
    }
    //初始化窗体布局
    private void initWindow() {
        SystemBarTintManager tintManager;
        //由于沉浸式状态栏需要在Android4.4.4以上才能使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorBackground));
            tintManager.setStatusBarTintEnabled(true);
        }
    }
    //点击事件
    @OnClick({R.id.add_people,R.id.roundButton,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_people:
                addContent(view);
                break;
            case R.id.roundButton:
                Request();
                finish();
                break;
        }
    }
    //动态添加控件
    private void addContent(View v) {
        View hotelEvaluateView = View.inflate(this, R.layout.item_players, null);
        linearLayout.addView(hotelEvaluateView);
        index++;

    }
    //获取动态添加的控件中的内容
    private String printData() {if (index==0){
        return "";
    }else{
        View childAt1 = linearLayout.getChildAt(0);
        EditText editText1 = (EditText) childAt1.findViewById(R.id.player_name);
        String standard=editText1.getText().toString();
        for (int i = 1; i < linearLayout.getChildCount(); i++) {
            View childAt = linearLayout.getChildAt(i);
            EditText editText = (EditText) childAt.findViewById(R.id.player_name);
            standard=standard+","+editText.getText().toString();
        }
        return  standard;
    }

    }
    //设置事件监听
    private void setListener() {
        //toolbar的点击事件
        settingToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    //网络请求
    public void Request() {
        //请求地址
        String url = API.SignUpEvent_URL;    //注①
        String tag = "SignUpEvent";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            String result = jsonObject.getString("Result");  //注④
                            if (result.equals("success")) {  //注⑤
                                Toast.makeText(getApplicationContext(), "报名成功",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "报名失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "无连接",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getApplicationContext(), "稍后重试",
                        Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("EventId", EventId);
                params.put("UserId", UserId);
                params.put("TeamName", TeamName.getText().toString());
                params.put("Participants", Captain.getText().toString()+","+printData());
                params.put("EventState", "2");
                params.put("TeamState", "1");
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}
