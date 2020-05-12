package com.stx.xhb.DCAPlatform.fragment.innerFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.stx.xhb.DCAPlatform.activities.MessageDetailActivity;
import com.stx.xhb.DCAPlatform.adapter.MessageListViewAdapter;
import com.stx.xhb.DCAPlatform.entity.ChapterListItem;
import com.stx.xhb.DCAPlatform.utils.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MessageFragment extends Fragment implements AdapterView.OnItemClickListener ,AdapterView.OnItemLongClickListener{
    @BindView(R.id.multiplestatusview)
    MultipleStatusView multiplestatusview;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    private View view;
    private ListView news_lv;
    private MessageListViewAdapter adapter;
    private LayoutInflater mInflater;
    private List<ChapterListItem> data = new ArrayList<>();
    //Android自带下拉刷新控件
    private TextView tv_title;
    private String UserId;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        initView();
        setAdapter();
        setListener();
        setSwipeRefreshInfo();
        return view;
    }
    //获取控件
    private void initView() {
        //获取到标题栏控件
        tv_title = (TextView) view.findViewById(R.id.title);
        tv_title.setText("消息");
        if (mInflater == null) {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        news_lv = (ListView) view.findViewById(R.id.content_view);
        adapter = new MessageListViewAdapter(getActivity(), data);
        UserId=getArguments().getString("UserId");
    }
    //下载网络数据
    public void Request() {
        multiplestatusview.showLoading();
        //请求地址
        String url = API.GetMessage_URL;    //注①
        String tag = "GetMessage";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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
                            JSONArray events = jsonObject.getJSONArray("Messages"); //注③
                            if (events.length()==0){
                                multiplestatusview.showEmpty();
                            }else{
                                data.clear();
                                for (int i = 0; i < events.length(); i++) {
                                    JSONObject jsonObject1 = events.getJSONObject(i);
                                    ChapterListItem chapterListItems = new ChapterListItem(jsonObject1.getString("EventName"),jsonObject1.getString("Content"),jsonObject1.getString("Time"),jsonObject1.getString("State"),jsonObject1.getString("Id"));
                                    data.add(chapterListItems);
                                }
                            }
                            ptrLayout.refreshComplete();

                        } catch (JSONException e) {
                            multiplestatusview.showError();
                            Toast.makeText(getActivity(), "无连接",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                            ptrLayout.refreshComplete();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                multiplestatusview.showError();
                Toast.makeText(getActivity(), "稍后重试",
                        Toast.LENGTH_SHORT).show();
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
    //设置适配器
    private void setAdapter() {
        news_lv.setAdapter(adapter);
    }
    //设置监听事件
    private void setListener() {
        //listview的条目事件点击
        news_lv.setOnItemClickListener(this);
        news_lv.setOnItemLongClickListener(this);
        //点击重试
        multiplestatusview.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request();
            }
        });
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
        View childAt1 = parent.getChildAt(position);
        ImageView imageView = (ImageView) childAt1.findViewById(R.id.red_point);
        imageView.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
        intent.putExtra("Id",data.get(position).getId());
        intent.putExtra("EventName",data.get(position).getEventname());
        intent.putExtra("Content",data.get(position).getContent());
        intent.putExtra("Time",data.get(position).getSenddate());
        intent.putExtra("State",data.get(position).getState());
        startActivity(intent);
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ConfirmDialog(data.get(position).getId());
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        { if (resultCode == 3)
        {   Request();
        }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    private void setSwipeRefreshInfo() {
        ptrLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(in.srain.cube.views.ptr.PtrFrameLayout frame, View content, View header) {
                return !canChildScrollUp();
            }

            @Override
            public void onRefreshBegin(in.srain.cube.views.ptr.PtrFrameLayout frame) {
                Request();
            }
        });
        ptrLayout.setLastUpdateTimeRelateObject(this);//设置是否显示上次更新时间
        ptrLayout.autoRefresh();//设置是否自动更新
    }
    /**
     * 判断是否滑动到顶端
     * @return
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (news_lv instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) news_lv;
                return absListView.getChildCount() > 0 &&
                        (absListView.getFirstVisiblePosition() > 0 ||
                                absListView.getChildAt(0).getTop() < absListView.getPaddingTop());

            } else {
                return ViewCompat.canScrollVertically(news_lv, -1) || news_lv.getScrollY() > 0;
            }

        } else {

            return ViewCompat.canScrollVertically(news_lv, -1);

        }

    }
    /**
     * 删除消息
     */
    //确认Dialog
    private void ConfirmDialog(final String id){
        dialog = new Dialog(getContext(), R.style.dialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);      //位于中间
        window.setWindowAnimations(R.style.dialog_share);    //弹出动画
        View inflate = View.inflate(getContext(), R.layout.confirm_dialog, null);
        TextView textView=inflate.findViewById(R.id.confirm_text);
        textView.setText("是否删除此条信息");
        inflate.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()){
                    dialog.dismiss();      //消失，退出
                }
            }
        });
        inflate.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()){
                   Request1(id);
                    dialog.dismiss();      //消失，退出
                }
            }
        });
        window.setContentView(inflate);
        //横向充满
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
    //删除请求
    public void Request1(final String Id) {
        multiplestatusview.showLoading();
        //请求地址
        String url = API.DeleteMessage_URL;    //注①
        String tag = "DeleteMessage";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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
                            String result = jsonObject.getString("Result");  //注④
                            if (result.equals("success")) {  //注⑤
                                Request();
                                Toast.makeText(getActivity(), "操作成功",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), "操作失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            multiplestatusview.showError();
                            Toast.makeText(getActivity(), "无连接",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                multiplestatusview.showError();
                Toast.makeText(getActivity(), "稍后重试",
                        Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Id", Id);  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
 }
