package com.kibisoftware.gallerytest;

import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kibi on 27/02/18.
 */

public class DetailsPage extends AppCompatActivity {

    private ImageView ivPicture;
    private ListView lvDetails;

    int position, picposition;

    public ArrayList<DetailsListItem> detailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.details_page);

        position = getIntent().getIntExtra("position", 0);
        picposition = getIntent().getIntExtra("picposition", 0);

        ivPicture = (ImageView) findViewById(R.id.iv_picture);
        lvDetails = (ListView) findViewById(R.id.lv_details);

        ArrayList<ImageFolder> folders = GalleryPage.imageFolders;
        String path = "file://" + folders.get(position).getimageUrls().get(picposition);
        Glide.with(this).load(path).into(ivPicture);

        ExifInterface exifInfo = null;
        try {
            exifInfo = new ExifInterface(folders.get(position).getimageUrls().get(picposition));
        } catch(IOException e) {
            e.printStackTrace();
        }

        if(exifInfo == null) {
            /* File doesn't exist or isn't an image */
            DetailsListItem item;
            item = new DetailsListItem("All details", "Unknown");
            detailsList.add(item);
            DetailsAdapter adapter = new DetailsAdapter(this, detailsList);
            lvDetails.setAdapter(adapter);
            return;
        }

        String dateString = exifInfo.getAttribute(ExifInterface.TAG_DATETIME);
        DetailsListItem item;
        if (dateString != null && dateString.length() > 0) {
            item = new DetailsListItem("Date created", dateString);
        } else {
            item = new DetailsListItem("Date created", "Unknown");
        }
        detailsList.add(item);

        String flash = exifInfo.getAttribute(ExifInterface.TAG_FLASH);
        if (flash != null && flash.length() > 0) {
            int flashNum = 0;
            try {
                flashNum = Integer.parseInt(flash);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
            if ((flashNum & 1) == 1) {
                item = new DetailsListItem("Flash", "Used Flash");
            } else {
                item = new DetailsListItem("Flash", "No Flash");
            }
        } else {
            item = new DetailsListItem("Flash", "No Flash");
        }
        detailsList.add(item);

        String latitude = exifInfo.getAttribute(ExifInterface.TAG_GPS_DEST_LATITUDE);
        if (latitude != null && latitude.length() > 0) {
            item = new DetailsListItem("Latitude", latitude);
        } else {
            item = new DetailsListItem("Latitude", "Unknown");
        }
        detailsList.add(item);

        String longitude = exifInfo.getAttribute(ExifInterface.TAG_GPS_DEST_LONGITUDE);
        if (longitude != null && longitude.length() > 0) {
            item = new DetailsListItem("Longitude", longitude);
        } else {
            item = new DetailsListItem("Longitude", "Unknown");
        }
        detailsList.add(item);

        String length = exifInfo.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
        String width = exifInfo.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
        if (length != null && length.length() > 0) {
            item = new DetailsListItem("Image size", length + " x " + width);
        } else {
            item = new DetailsListItem("Image size", "Unknown");
        }
        detailsList.add(item);

        String location = exifInfo.getAttribute(ExifInterface.TAG_SUBJECT_LOCATION);
        if (location != null && location.length() > 0) {
            item = new DetailsListItem("Location", location);
        } else {
            item = new DetailsListItem("Location", "Unknown");
        }
        detailsList.add(item);

        DetailsAdapter adapter = new DetailsAdapter(this, detailsList);
        lvDetails.setAdapter(adapter);
    }
}
