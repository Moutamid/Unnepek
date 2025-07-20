package com.moutamid.unnepek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class ColorAdapter extends BaseAdapter {

    private final Context context;
    private final int[] colors;
    private final String[] colorNames;

    public ColorAdapter(Context context, int[] colors, String[] colorNames) {
        this.context = context;
        this.colors = colors;
        this.colorNames = colorNames;
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int position) {
        return colors[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_color_option, parent, false);
        }

        View colorBox = convertView.findViewById(R.id.colorBox);
        TextView colorName = convertView.findViewById(R.id.colorName);

        colorBox.setBackgroundColor(colors[position]);
        colorName.setText(colorNames[position]);

        return convertView;
    }
}
