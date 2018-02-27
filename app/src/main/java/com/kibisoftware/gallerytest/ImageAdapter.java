package com.kibisoftware.gallerytest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by kibi on 26/02/18.
 */

public class ImageAdapter extends ArrayAdapter<ImageFolder> {
    private Context context;
    private ViewInfo vi;
    private ArrayList<ImageFolder> folders = new ArrayList<>();

    public ImageAdapter(Context context, ArrayList<ImageFolder> folders) {
        super(context, R.layout.gallery_item, folders);
        this.context = context;
        this.folders = folders;
    }

    @Override
    public int getCount() {
        return folders.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (folders.size() > 0) {
            return folders.size();
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

        vi.tv_folderName.setText(folders.get(position).getFolderName());
        vi.tv_folderSize.setText(folders.get(position).getimageUrls().size()+"");



        Glide.with(context).load("file://" + folders.get(position).getimageUrls().get(0))
                .into(vi.iv_image);


        return convertView;

    }

    private static class ViewInfo {
        TextView tv_folderName, tv_folderSize;
        ImageView iv_image;
    }

}
