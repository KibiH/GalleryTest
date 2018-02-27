package com.kibisoftware.gallerytest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kibi on 28/02/18.
 */

public class DetailsAdapter extends ArrayAdapter<DetailsListItem> {

    private Context context;
    private TextView tvTitle, tvValue;

    public DetailsAdapter(@NonNull Context context, ArrayList<DetailsListItem> items) {
        super(context, 0, items);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        DetailsListItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.details_item, parent, false);
        }

        tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        tvValue = (TextView) convertView.findViewById(R.id.tv_value);

        tvTitle.setText(item.title);
        tvValue.setText(item.info);

        return convertView;

    }

}
