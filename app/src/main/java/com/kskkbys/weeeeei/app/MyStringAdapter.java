package com.kskkbys.weeeeei.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by keisuke on 2014/06/29.
 */
public class MyStringAdapter extends ArrayAdapter<String> {

    private static final int[] COLORS = {
            R.color.pomegranate,
            R.color.alizarin,
            R.color.pumpkin,
            R.color.orange,
            R.color.nephritis,
            R.color.turquoise,
            R.color.peter_river,
            R.color.belize_hole,
            R.color.wet_asphalt
    };

    public MyStringAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public MyStringAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        // positionによって背景色を変える
        v.setBackgroundResource(COLORS[position % COLORS.length]);

        return v;
    }
}
