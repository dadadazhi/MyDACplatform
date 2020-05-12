package com.stx.xhb.DCAPlatform.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendMessageActivity extends AppCompatActivity {

    Toolbar settingToolbar;
    private Toast mToast;
    private ImageView imageView1,imageView2;
    private EditText content;
    private TextView textView;
    private String Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ButterKnife.bind(this);
        initWindow();
        initView();
        setListener();

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

    //初始化控件
    private void initView() {
        settingToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(settingToolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        //设置显示返回上一级的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置标题
        getSupportActionBar().setTitle("发布公告");
        //设置标题栏字体颜色
        settingToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        settingToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        imageView1=(ImageView)findViewById(R.id.select1);
        imageView2=(ImageView)findViewById(R.id.select2);
        content=(EditText)findViewById(R.id.am_content);
        textView=(TextView)findViewById(R.id.am_title);

    }

    //点击事件
    @OnClick({R.id.create_button,R.id.am_title,R.id.select1,R.id.select2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.am_title:
                startActivityForResult(new Intent(SendMessageActivity.this, SelectEventActivity.class), 1);
                break;
            case R.id.create_button:
                if(imageView1.getTag().equals("yes")){
                    Request("1");
                }
                if (imageView2.getTag().equals("yes")){
                    Request("2");
                }
                break;
            case R.id.select1:
                if(imageView1.getTag().equals("yes")){
                    imageView1.setImageResource(R.drawable.select_1);
                    imageView1.setTag("no");
                }else{
                    imageView1.setImageResource(R.drawable.select_2);
                    imageView1.setTag("yes");
                }
                break;
            case R.id.select2:
                if(imageView2.getTag().equals("yes")){
                    imageView2.setImageResource(R.drawable.select_1);
                    imageView2.setTag("no");
                }else{
                    imageView2.setImageResource(R.drawable.select_2);
                    imageView2.setTag("yes");
                }
                break;
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

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void showTip(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }
    //网络请求
    public void Request(final String Recipient) {
        //请求地址
        String url = API.NewMessage_URL;    //注①
        String tag = "NewMessage";    //注②

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
                                Toast.makeText(getApplicationContext(), "发布成功",
                                        Toast.LENGTH_SHORT).show();
                                 finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "发布失败",
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
                params.put("Id", Id);
                params.put("EventName", textView.getText().toString());
                params.put("Content", content.getText().toString());
                params.put("Recipient", Recipient);
                params.put("Time",getTime());
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            { if (resultCode == 2)
                {   textView.setText(data.getStringExtra("EventName"));
                    Id=data.getStringExtra("EventId");
                }
            }
        }
    private String getTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm"); //设置时间格式
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08")); //设置时区
        Date curDate = new Date(System.currentTimeMillis()); //获取当前时间
        String createDate = formatter.format(curDate);   //格式转换
        return createDate;
    }
}


