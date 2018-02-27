package com.kibisoftware.gallerytest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by kibi on 27/02/18.
 */

public class GridViewAdapter extends ArrayAdapter<ImageFolder> {
    private Context context;
    private ViewInfo vi;
    private ArrayList<ImageFolder> folders = new ArrayList<>();
    private int galleryPosition;

    public GridViewAdapter(@NonNull Context context, ArrayList<ImageFolder> folders, int galleryPosition) {
        super(context, R.layout.gallery_item, folders);
        this.context = context;
        this.folders = folders;
        this.galleryPosition = galleryPosition;
    }

    @Override
    public int getCount() {
        return folders.get(galleryPosition).getimageUrls().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (folders.get(galleryPosition).getimageUrls().size() > 0) {
            return folders.get(galleryPosition).getimageUrls().size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            vi = new ViewInfo();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_item, parent, false);
            vi.tv_folderName = (TextView) convertView.findViewById(R.id.tv_folder);
            vi.tv_folderSize = (TextView) convertView.findViewById(R.id.tv_folder_size);
            vi.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);


            convertView.setTag(vi);
        } else {
            vi = (ViewInfo) convertView.getTag();
        }

        vi.tv_folderName.setVisibility(View.GONE);
        vi.tv_folderSize.setVisibility(View.GONE);

        Glide.with(context).load("file://" + folders.get(galleryPosition).getimageUrls().get(position))
                .into(vi.iv_image);

        return convertView;

    }

    private static class ViewInfo {
        TextView tv_folderName, tv_folderSize;
        ImageView iv_image;
    }
}
