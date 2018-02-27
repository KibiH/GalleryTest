package com.kibisoftware.gallerytest;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


/**
 * Created by kibi on 26/02/18.
 */

public class GalleryPage extends AppCompatActivity {
    public static ArrayList<ImageFolder> imageFolders = new ArrayList<>();
    private GridView gridview = null;
    private Cursor cursor = null;
    private ProgressDialog progressDialog = null;

    private static final int REQUEST_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery);

        gridview = (GridView) findViewById(R.id.gridview);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(GalleryPage.this, SubGalleryPage.class);
                i.putExtra("position", position);
                startActivity(i);
            }
        });


        // We're going to be looking through the phone to find all the pictures, so we
        // need permission to read storage outside the app
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog dialog = builder.setMessage(getString(R.string.permissions_message)).
                    setCancelable(false).
                    setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(GalleryPage.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_PERMISSIONS);
                            dialog.cancel();
                        }
                    }).
                    create();
                dialog.show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            // already have permission
            loadImages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        loadImages();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        AlertDialog dialog = builder.setMessage(getString(R.string.permission_fail)).
                                setCancelable(false).
                                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        GalleryPage.this.finish();
                                        dialog.cancel();
                                    }
                                }).
                                create();
                        dialog.show();
                    }
                }
            }
        }
    }

    private void loadImages() {
        imageFolders.clear();

        // this Uri will set the cursor to find all Images in the phone
        Uri picsUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // this projection will allow us to see the folder name each pic sits in
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        cursor = getContentResolver().query(picsUri, projection, null, null, orderBy);
        final int colIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        final int colIndexFolderName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        if (cursor != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();

            // major work of indexing pics occurs here
            // the idea id to arrange lists of pics within the folders they come in
            // we could try to find just ones directly from the camera, but firstly, thr camera
            // folder name differs by phone, secondly, nowadays people often edit photos and the
            // output goes into another folder and thirdly, all the cool gallery apps now
            // show you multiple folders to look through
            while (cursor.moveToNext()) {
                String pathOfImage = cursor.getString(colIndexData);
                String folderName = cursor.getString(colIndexFolderName);

                // check which folder it is in
                boolean existingFolder = false;
                ImageFolder currentFolder = null;
                for (ImageFolder folder: imageFolders) {
                    if (folder.getFolderName().equals(folderName)) {
                        existingFolder = true;
                        currentFolder = folder;
                        break;
                    }
                }

                if (existingFolder) {
                    ArrayList<String> urls = new ArrayList<>();
                    urls.addAll(currentFolder.getimageUrls());
                    urls.add(pathOfImage);
                    currentFolder.setImageUrls(urls);
                } else {
                    // we need a new folder
                    ArrayList<String> urls = new ArrayList<>();
                    urls.add(pathOfImage);
                    currentFolder = new ImageFolder();
                    currentFolder.setFolderName(folderName);
                    currentFolder.setImageUrls(urls);
                    imageFolders.add(currentFolder);
                }
            }
            gridview.setAdapter(new ImageAdapter(GalleryPage.this, imageFolders));

            progressDialog.dismiss();
        }

    }
}
