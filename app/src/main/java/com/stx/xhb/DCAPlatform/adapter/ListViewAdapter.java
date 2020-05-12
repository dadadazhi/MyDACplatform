package com.stx.xhb.DCAPlatform.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stx.xhb.DCAPlatform.R;
import com.stx.xhb.DCAPlatform.entity.ChapterListItem;

import java.util.List;


public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private List<ChapterListItem> chapterListItems;
    private LayoutInflater mLayoutInflater;

    public ListViewAdapter(Context context, List<ChapterListItem> chapterListItems) {
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.listview_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
            //设置tag
            convertView.setTag(viewHolder);
        } else {
            //获取缓存布局
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ChapterListItem chapterListItem = chapterListItems.get(position);
        viewHolder.title.setText(chapterListItem.getEventname());
        viewHolder.date.setText(chapterListItem.getStarttime());//文章发布时间
        //viewHolder.iv.setImageResource(R.drawable.product_default);//设置默认图片
        final ImageView iv = viewHolder.iv;
        //获取到图片地址
        Bitmap image  =chapterListItem.getImage();
        //如果图片地址为空，则设置默认图片
        if (image == null) {
            iv.setImageResource(R.drawable.product_default);
        }else {
            iv.setImageBitmap(image);
        }

        return convertView;
    }

    //创建一个ViewHolder保存converview的布局
    class ViewHolder {
        ImageView iv;//图片
        //标题、日期、评论数、文章id、分类id、文章地址
        TextView title, date, comment, tv_id, tv_typeid, tv_url;
    }

}
