package com.stx.xhb.DCAPlatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stx.xhb.DCAPlatform.R;
import com.stx.xhb.DCAPlatform.entity.ChapterListItem;

import java.util.List;
//消息的适配器
public class MessageListViewAdapter extends BaseAdapter {
    private Context context;
    private List<ChapterListItem> chapterListItems;
    private LayoutInflater mLayoutInflater;

    public MessageListViewAdapter(Context context, List<ChapterListItem> chapterListItems) {
        this.context = context;
        this.chapterListItems = chapterListItems;
        if (mLayoutInflater == null) {
            mLayoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }
    @Override
    public int getCount() {
        return chapterListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return chapterListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        MessageListViewAdapter.ViewHodler viewHodler = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item_message, parent, false);
            viewHodler = new ViewHodler();
            viewHodler.me_title = (TextView) convertView.findViewById(R.id.me_title);
            viewHodler.me_date = (TextView) convertView.findViewById(R.id.me_date);
            viewHodler.me_content = (TextView) convertView.findViewById(R.id.me_content);
            viewHodler.red_point = (ImageView) convertView.findViewById(R.id.red_point);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (MessageListViewAdapter.ViewHodler) convertView.getTag();
        }
            ChapterListItem chapterListItem = chapterListItems.get(position);
            viewHodler.me_title.setText(chapterListItem.getEventname());
            viewHodler.me_date.setText(chapterListItem.getSenddate());//时间
            viewHodler.me_content.setText(chapterListItem.getContent());//内容
            if(chapterListItem.getState().equals("2")){
                viewHodler.red_point.setVisibility(View.INVISIBLE);
            }else {
                viewHodler.red_point.setVisibility(View.VISIBLE);

            }
        return convertView;
    }

    //创建一个ViewHolder保存缓存布局
    class ViewHodler {
        TextView me_title,me_date,me_content;
        ImageView red_point;
    }
}

