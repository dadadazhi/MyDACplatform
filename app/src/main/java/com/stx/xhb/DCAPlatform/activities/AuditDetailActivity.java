package com.stx.xhb.DCAPlatform.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.stx.xhb.DCAPlatform.utils.FileUtils;
import com.stx.xhb.DCAPlatform.utils.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuditDetailActivity extends AppCompatActivity {

    private TextView tv_title;
    private LinearLayout linearLayout;
    private String Standards,Word,VideoUrl,Id,Type;
    private File file;
    private String[] Standard;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_detail);
        Intent intent=getIntent();
        Standards=intent.getStringExtra("Standard");
        Word=intent.getStringExtra("Word");
        VideoUrl=intent.getStringExtra("VideoUrl");
        Id=intent.getStringExtra("Id");
        Type=intent.getStringExtra("Type");
        initWindow();
        ButterKnife.bind(this);
        initView();
    }
    //初始化控件，获取控件
    private void initView() {
        tv_title = (TextView)findViewById(R.id.title);
        tv_title.setText("审核");
        linearLayout = (LinearLayout) findViewById(R.id.ad_content_view);

        button=(Button)findViewById(R.id.roundButton);
        Standard=Standards.split(",");
        for(int i = 0; i < Standard.length; i=i+2)
        {
            addContent(Standard[i],Standard[i+1]);
        }
        String s = Word;
        int i = s.lastIndexOf("/");
        String FileName= s.substring(i + 1);
        file =new File(Environment.getExternalStorageDirectory().toString() + "/DACplatform",FileName);
        if(Type.equals("2")){
            button.setText("修改分数");
        }
        else {
                    if(!file.exists()){
                        downLoad("http://120.79.231.209:8080/download/"+Word,FileName);
                    }

            }

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
    @OnClick({R.id.aa_video,R.id.aa_word,R.id.roundButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aa_word:
                Intent intent = getWordFileIntent(file.getPath());
                try {
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(this,"找不到可以打开该文件的程序", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.aa_video:
                Uri uri = Uri.parse(VideoUrl);
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;
            case R.id.roundButton:
                if(printData()==1000){
                    Toast.makeText(getApplicationContext(), "单项给分不可以超过限定分值",
                            Toast.LENGTH_LONG).show();
                }else if ((printData()==1001)){
                    Toast.makeText(getApplicationContext(), "请先打分再完成",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Request(printData()+"");
                    finish();
                }


                break;
        }
    }
    //下载说明文档
    /**
     * 从服务器下载文件
     * @param path 下载文件的地址
     * @param FileName 文件名字
     */
    public static void downLoad(final String path, final String FileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();//获取输入流
                        FileOutputStream fileOutputStream = null;//文件输出流
                        if (is != null) {
                            FileUtils fileUtils = new FileUtils();
                            fileOutputStream = new FileOutputStream(fileUtils.createFile(FileName));//指定文件保存路径，代码看下一步
                            byte[] buf = new byte[1024];
                            int ch;
                            while ((ch = is.read(buf)) != -1) {
                                fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                            }
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //网络请求
    public void Request(final String data) {
        //请求地址
        String url = API.SetScore_URL;    //注①
        String tag = "SetScore";    //注②

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
                                Toast.makeText(getApplicationContext(), "上传成功",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "上传失败",
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
                params.put("Score", data);  //注⑥
                params.put("Id", Id);
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
    //android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param ) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }
    //动态添加控件
    private void addContent(String Standard,String Score) {
         View hotelEvaluateView = View.inflate(this, R.layout.item_audit_standard, null);
         final TextView textView=hotelEvaluateView.findViewById(R.id.te_standard);
         final TextView textView1=hotelEvaluateView.findViewById(R.id.tv_score);
         textView.setText(Standard+":   ");
         textView1.setText(Score);
         linearLayout.addView(hotelEvaluateView);
    }
    //获取总分数
    private int printData() {
        int score=0;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View childAt = linearLayout.getChildAt(i);
            EditText editText = (EditText) childAt.findViewById(R.id.tv_standard);
            TextView textView=(TextView) childAt.findViewById(R.id.tv_score);
            if(editText.getText().toString().equals("")){
                return 1001;
            }
            if(Integer.parseInt(editText.getText().toString())>Integer.parseInt(textView.getText().toString())){
                return 1000;
            }

            score=score+Integer.parseInt(editText.getText().toString());
        }
        return score;
    }
}
