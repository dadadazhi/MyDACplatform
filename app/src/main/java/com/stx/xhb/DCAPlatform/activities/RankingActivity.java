package com.stx.xhb.DCAPlatform.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class RankingActivity extends AppCompatActivity {

    private TextView tv_title;
    private LinearLayout linearLayout;
    private String EventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Intent intent=getIntent();
        EventId=intent.getStringExtra("EventId");
        initWindow();
        ButterKnife.bind(this);
        initView();
        Request();
    }
    //初始化控件，获取控件
    private void initView() {
        tv_title = (TextView)findViewById(R.id.title);
        tv_title.setText("排名");
        linearLayout = (LinearLayout) findViewById(R.id.ad_content_view);
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
    //网络请求
    public void Request() {
        //请求地址
        String url = API.GetRanking_URL;    //注①
        String tag = "GetRanking";    //注②

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
                            JSONArray scores = jsonObject.getJSONArray("Events");
                            for (int i = 0; i < scores.length(); i++) {
                                JSONObject jsonObject1 = scores.getJSONObject(i);
                                addContent(i+1+"",jsonObject1.getString("TeamName"),jsonObject1.getString("Captain"));
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
                params.put("EventId", EventId);  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
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
    //动态添加控件
    private void addContent(String Ranking,String Name,String Captain) {
        View hotelEvaluateView = View.inflate(this, R.layout.item_ranking, null);
        final TextView textView=hotelEvaluateView.findViewById(R.id.te_ranking);
        final TextView textView1=hotelEvaluateView.findViewById(R.id.te_name);
        final TextView textView2=hotelEvaluateView.findViewById(R.id.te_captain);
        textView.setText(Ranking);
        textView1.setText(Name);
        textView2.setText(Captain);
        linearLayout.addView(hotelEvaluateView);
    }
}
