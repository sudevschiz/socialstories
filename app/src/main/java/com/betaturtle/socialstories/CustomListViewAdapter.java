package com.betaturtle.socialstories;

/**
 * Written originally by Neo for Bline
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {
    Context context;
    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }
    /*private view holder class*/
    private class ViewHolder {
        TextView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_list, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.secondLine);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.firstLine);
            holder.imageView = (TextView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.txtDesc.setText(rowItem.getDesc());
        holder.txtTitle.setText(rowItem.getTitle());
//holder.imageView.setImageResource(rowItem.getImageId());
        holder.imageView.setText(rowItem.getImageId());
        return convertView;
    }
}