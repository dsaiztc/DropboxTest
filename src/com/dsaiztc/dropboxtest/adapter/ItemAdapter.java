package com.dsaiztc.dropboxtest.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsaiztc.dropboxtest.R;

public class ItemAdapter extends BaseAdapter
{
	private final String TAG = "ItemAdapter";
	
	private Context context;
	private List<ListItem> items;

	public ItemAdapter(Context context, List<ListItem> items)
	{
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public Object getItem(int position)
	{
		return items.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		ListItem item = items.get(position);
		
		// TODO
		//return item.getId();

		Log.e(TAG, "getItemId(" + position + ")");
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;

		if (convertView == null)
		{
			// Create a new view into the list.
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_item, parent, false);
		}

		// Set data into the view.
		ImageView icon = (ImageView) rowView.findViewById(R.id.list_item_icon);
		TextView firstLine = (TextView) rowView.findViewById(R.id.list_item_firstLine);
		TextView secondLine = (TextView) rowView.findViewById(R.id.list_item_secondLine);

		ListItem item = this.items.get(position);
		if (item.getIcon() != -1)
		{
			icon.setImageResource(item.getIcon());
		}
		else
		{
			icon.setImageDrawable(item.getIconDrawable());
		}
		firstLine.setText(item.getFirstLine());
		secondLine.setText(item.getSecondLine());

		return rowView;
	}
}