package com.stx.xhb.DCAPlatform;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iflytek.autoupdate.IFlytekUpdate;
import com.stx.xhb.DCAPlatform.adapter.MainFragmentPageAdapter;
import com.stx.xhb.DCAPlatform.base.BaseApplication;
import com.stx.xhb.DCAPlatform.fragment.AdministratorFragment;
import com.stx.xhb.DCAPlatform.fragment.AuditFragment;
import com.stx.xhb.DCAPlatform.fragment.MineFragment;
import com.stx.xhb.DCAPlatform.fragment.innerFragments.MessageFragment;
import com.stx.xhb.DCAPlatform.fragment.innerFragments.NewsFragment;
import com.stx.xhb.DCAPlatform.utils.API;
import com.stx.xhb.DCAPlatform.utils.SystemBarTintManager;
import com.stx.xhb.DCAPlatform.view.BadgeHelper;
import com.stx.xhb.DCAPlatform.view.TipsToast;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private ViewPager main_viewPager;
    private List<Fragment> fragemnts = new ArrayList<>();
    private MainFragmentPageAdapter adapter;
    private RadioGroup rgp;
    private TipsToast tipsToast;
    //退出时间
    private long exitTime = 0;
    private IFlytekUpdate mUpdManager;
    private String Type;
    private String UserId;
    private Button button;
    private BaseApplication baseApplication;
    private int message=0;
    private BadgeHelper badgeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Request();
        initWindow();
        initView();
        initData();
        setAdapter();
        setListener();

    }



    @Override
    protected void onResume() {
        super.onResume();
        Request();
        MobclickAgent.onResume(this);
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
        main_viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        //设置viewpager的预加载页数，viewpager默认只会预加载左右两边的页面数据
        main_viewPager.setOffscreenPageLimit(4);
        rgp = (RadioGroup) findViewById(R.id.main_rgp);
        //设置默认第一个为选中状态
        RadioButton rb = (RadioButton) rgp.getChildAt(0);
        rb.setChecked(true);
        baseApplication=(BaseApplication)getApplication();
        Type=baseApplication.getType();
        UserId=baseApplication.getUserId();
        button=(Button)findViewById(R.id.message);
        badgeHelper = new BadgeHelper(this)
                .setBadgeType(BadgeHelper.Type.TYPE_TEXT)
                .setBadgeOverlap(false);
        badgeHelper.bindToTargetView(button);



    }

    //初始化数据
    private void initData() {
        NewsFragment newsFragment=new NewsFragment();
        Bundle args=new Bundle();
        args.putString("UserId",UserId);
        args.putString("Type",Type);
        newsFragment.setArguments(args);
        MessageFragment messageFragment =new MessageFragment();
        Bundle args1=new Bundle();
        args1.putString("UserId",UserId);
        messageFragment.setArguments(args1);
        fragemnts.add(newsFragment);//赛事
        fragemnts.add(messageFragment);//消息
        switch (Type){
            case "1":
                MineFragment mine_fragment=new MineFragment();
                fragemnts.add(mine_fragment);//我的
                break;
            case "2":
                AuditFragment audit_fragment=new AuditFragment();
                fragemnts.add(audit_fragment);//审核
                break;
            case "3":
                AdministratorFragment administrator_fragment=new AdministratorFragment();
                fragemnts.add(administrator_fragment);//管理
                break;}
    }

    //设置适配器
    private void setAdapter() {
        //实例化适配器
        adapter = new MainFragmentPageAdapter(getSupportFragmentManager(), fragemnts);
        //设置适配器
        main_viewPager.setAdapter(adapter);
    }

    //设置监听
    private void setListener() {
        //viewPager的滑动监听
        main_viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //获取当前位置的RadioButton
                RadioButton rb = (RadioButton) rgp.getChildAt(position);
                //设置为true
                rb.setChecked(true);
            }
        });
        //RadioGroup的事件监听
        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int index = 0; index < rgp.getChildCount(); index++) {
                    RadioButton rb = (RadioButton) rgp.getChildAt(index);
                    if (rb.isChecked()) {
                        main_viewPager.setCurrentItem(index, false);
                        break;
                    }
                }
            }
        });
    }

    /**
     * 按两次退出应用
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showTips(R.drawable.tips_smile, "再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    /**
     * 自定义toast
     *
     * @param iconResId 图片
     * @param tips      提示文字
     */
    private void showTips(int iconResId, String tips) {
        if (tipsToast != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                tipsToast.cancel();
            }
        } else {
            tipsToast = TipsToast.makeText(MainActivity.this.getApplication()
                    .getBaseContext(), tips, TipsToast.LENGTH_SHORT);
        }
        tipsToast.show();
        tipsToast.setIcon(iconResId);
        tipsToast.setText(tips);
    }
    //下载网络数据
    public void Request() {
        String url = API.GetMessage_URL;    //注①
        String tag = "GetMessage";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            message=0;
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            JSONArray events = jsonObject.getJSONArray("Messages"); //注③
                            for (int i = 0; i < events.length(); i++) {
                                JSONObject jsonObject1 = events.getJSONObject(i);
                                if(jsonObject1.getString("State").equals("1")){
                                    message++;
                                }
                            }
                            if(message==0){
                                badgeHelper.setVisibility(View.INVISIBLE);
                            }else {
                                badgeHelper.setBadgeNumber(message);
                            }
                        } catch (JSONException e) {
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId", UserId);  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

}
