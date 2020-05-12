package com.stx.xhb.DCAPlatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stx.xhb.DCAPlatform.R;
import com.stx.xhb.DCAPlatform.entity.ChapterListItem;

import java.util.List;

//审核队伍适配器
public class AuditAdapter extends BaseAdapter {
    private Context context;
    private List<ChapterListItem> chapterListItems;
    private LayoutInflater mLayoutInflater;

    public AuditAdapter(Context context, List<ChapterListItem> chapterListItems) {
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
        AuditAdapter.ViewHodler viewHodler = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item_audit, parent, false);
            viewHodler = new AuditAdapter.ViewHodler();
            viewHodler.ia_event_name = (TextView) convertView.findViewById(R.id.ia_event_name);
            viewHodler.ia_team_name = (TextView) convertView.findViewById(R.id.ia_team_name);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (AuditAdapter.ViewHodler) convertView.getTag();
        }
        ChapterListItem chapterListItem = chapterListItems.get(position);
        viewHodler.ia_event_name.setText(chapterListItem.getEventname());//赛事名称
        viewHodler.ia_team_name.setText(chapterListItem.getTeamname());//参赛队伍名称
        return convertView;
    }

    //创建一个ViewHolder保存缓存布局
    class ViewHodler {
        TextView ia_captain_name,ia_team_name,ia_event_name;
    }
}


