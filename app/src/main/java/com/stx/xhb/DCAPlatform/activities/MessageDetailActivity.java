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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stx.xhb.DCAPlatform.R;
import com.stx.xhb.DCAPlatform.utils.API;
import com.stx.xhb.DCAPlatform.utils.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class MessageDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private String Id,EventName,Content,Time,State;
    private TextView textView,textView1,textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_datail);
        Intent intent=getIntent();
        Id=intent.getStringExtra("Id");
        EventName=intent.getStringExtra("EventName");
        Content=intent.getStringExtra("Content");
        Time=intent.getStringExtra("Time");
        State=intent.getStringExtra("State");
        Intent intent1 =new Intent();
        setResult(3,intent1);
        initWindow();
        ButterKnife.bind(this);
        initView();
        setListener();
        if (State.equals("1")){
            Request();
        }


    }

    //初始化控件，获取控件
    private void initView() {
        //获取控件
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //2.替代
        setSupportActionBar(toolbar);
        //加载背景颜色
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        //设置显示返回上一级的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置标题
        getSupportActionBar().setTitle("消息详情");
        //设置标题栏字体颜色
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        textView=(TextView)findViewById(R.id.am_title);
        textView1=(TextView)findViewById(R.id.am_content);
        textView2=(TextView)findViewById(R.id.am_date);
        textView.setText(EventName);
        textView1.setText(Content);
        textView2.setText(Time);


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

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);       //统计时长
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPause(this);
    }
    //设置监听
    private void setListener() {
        //设置toolbar返回上一级的事件监听
        toolbar.setNavigationOnClickListener(this);
    }

    //toolbar的事件监听
    @Override
    public void onClick(View v) {
        //返回上一级

        finish();
    }
    //网络请求
    public void Request() {
        //请求地址
        String url = API.ChangeMessage_URL;    //注①
        String tag = "ChangeMessage";    //注②

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


                            } else {

                            }
                        } catch (JSONException e) {
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getApplicationContext(), "稍后重试1",
                        Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Id", Id);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}

