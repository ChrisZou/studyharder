/**
 * AppsAdapter.java
 * 
 * Created by zouyong on 7:19:01 AM, 2014
 */
package com.chriszou.studyharder;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chriszou.androidlibs.AppInfo;
import com.chriszou.androidlibs.AppManager;
import com.chriszou.androidlibs.BaseListAdapter;

/**
 * @author zouyong
 * 
 */
public class AppsAdapter extends BaseListAdapter<AppInfo> {

    AppManager mAppManager;
	/**
	 * @param context
	 * @param data
	 */
	public AppsAdapter(Context context, List<AppInfo> data) {
		super(context, data);
        mAppManager = new AppManager(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
		if (convertView == null) {
            convertView = mInflater.inflate(R.layout.app_item, null);
            holder = new ViewHolder();
            holder.iconView = (ImageView) convertView.findViewById(R.id.app_item_icon);
            holder.nameView = (TextView)convertView.findViewById(R.id.app_item_name);
            holder.packageView = (TextView) convertView.findViewById(R.id.app_item_package);
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

        AppInfo info = getItem(position);
        holder.nameView.setText(info.name);
        holder.packageView.setText(info.pkgName);
        holder.iconView.setImageDrawable(info.icon);
		return convertView;
	}
    
	public static class ViewHolder {
		public ImageView iconView;
        public TextView nameView;
        public TextView packageView;
	}

}
