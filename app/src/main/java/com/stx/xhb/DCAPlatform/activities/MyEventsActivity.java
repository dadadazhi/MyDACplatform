package com.stx.xhb.DCAPlatform.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.classic.common.MultipleStatusView;
import com.stx.xhb.DCAPlatform.R;
import com.stx.xhb.DCAPlatform.adapter.ListViewAdapter;
import com.stx.xhb.DCAPlatform.base.BaseApplication;
import com.stx.xhb.DCAPlatform.entity.ChapterListItem;
import com.stx.xhb.DCAPlatform.utils.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import sun.misc.BASE64Decoder;

public class MyEventsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    MultipleStatusView multiplestatusview;
    private ListView news_lv;
    private ListViewAdapter adapter;
    private View mFootView;
    private List<ChapterListItem> data = new ArrayList<>();
    //Android自带下拉刷新控件
    private List<ChapterListItem> chapterListItems;
    private int currenPage = 1;//当前页
    private boolean isBottom;//是否到底部的标记
    private boolean isLoadData = false;//判断是否已经在加载数据
    private String url;
    private Toolbar toolbar;
    private String Type,UserId;
    private BaseApplication baseApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent=getIntent();
        Type=intent.getStringExtra("Type");
        ButterKnife.bind(this);
        initView();
        setAdapter();
        setListener();

    }
    //获取控件
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.ae_toolbar);
        multiplestatusview=(MultipleStatusView)findViewById(R.id.ae_multiplestatusview) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        //设置显示返回上一级的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置标题
        switch (Type){
            case "1":
                getSupportActionBar().setTitle("已报名");
                Request("2");
                break;
            case "2":
                getSupportActionBar().setTitle("进行中");
                Request("3");
                break;
            case "3":
                getSupportActionBar().setTitle("已结束");
                Request1();
                break;

        }

        //设置标题栏字体颜色
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        news_lv = (ListView) findViewById(R.id.ae_content_view);
        adapter = new ListViewAdapter(MyEventsActivity.this, data);
        baseApplication=(BaseApplication)getApplication();
        UserId=baseApplication.getUserId();
    }
    //设置事件监听
    private void setListener() {
        //toolbar的点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //listview的条目事件点击
        news_lv.setOnItemClickListener(this);

    }
    //下载网络数据
    public void Request(final String State) {
        multiplestatusview.showLoading();
        //请求地址
        String url = API.UserEvent_URL;    //注①
        String tag = "UserEvent";    //注②

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
                            multiplestatusview.showContent();
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            JSONArray events = jsonObject.getJSONArray("Events"); //注③
                            data.clear();
                            for (int i = 0; i < events.length(); i++) {
                                JSONObject jsonObject1 = events.getJSONObject(i);
                                ChapterListItem chapterListItems = new ChapterListItem(jsonObject1.getString("EventName"),jsonObject1.getString("StartDate"),stringImage(jsonObject1.getString("Image")),jsonObject1.getString("Id"));
                                data.add(chapterListItems);
                            }
                            news_lv.setAdapter(adapter);
                        } catch (JSONException e) {
                            multiplestatusview.showError();
                            Toast.makeText(getApplicationContext(), "无连接",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                multiplestatusview.showError();
                Toast.makeText(getApplicationContext(), "稍后重试",
                        Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("State", State);  //注⑥
                params.put("UserId", UserId);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    public void Request1() {
        multiplestatusview.showLoading();
        //请求地址
        String url = API.UserEnd_URL;    //注①
        String tag = "UserEnd";    //注②

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
                            multiplestatusview.showContent();
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            JSONArray events = jsonObject.getJSONArray("Events"); //注③
                            for (int i = 0; i < events.length(); i++) {
                                JSONObject jsonObject1 = events.getJSONObject(i);
                                ChapterListItem chapterListItems = new ChapterListItem(jsonObject1.getString("EventName"),jsonObject1.getString("StartDate"),stringImage(jsonObject1.getString("Image")),jsonObject1.getString("Id"));
                                data.add(chapterListItems);
                            }
                            news_lv.setAdapter(adapter);
                        } catch (JSONException e) {
                            multiplestatusview.showError();
                            Toast.makeText(getApplicationContext(), "无连接",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                multiplestatusview.showError();
                Toast.makeText(getApplicationContext(), "稍后重试",
                        Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    //设置适配器
    private void setAdapter() {
        news_lv.setAdapter(adapter);
    }
    /**
     * listview的点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击条目跳转到详情界面
        String eventid = data.get(position ).getEventid();//获取到赛事id
        Intent intent = new Intent(MyEventsActivity.this, EventDetailActivity.class);
        intent.putExtra("EventId",eventid);
        startActivity(intent);

    }
    //将Bitmap转化为图片
    public Bitmap stringImage(String imgStr) {
        // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null)
            return null;
        try {
            // Base64解码
            byte[] b = new BASE64Decoder().decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    // 调整异常数据
                    b[i] += 256;
                }
            }
            return BitmapFactory.decodeByteArray(b, 0, b.length, null);
        } catch (Exception e) {
            return null;
        }
    }
}
