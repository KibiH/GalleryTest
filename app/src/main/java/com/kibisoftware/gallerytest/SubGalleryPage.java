package com.kibisoftware.gallerytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by kibi on 27/02/18.
 */

public class SubGalleryPage extends AppCompatActivity {
    private int position;
    private GridView gridview;
    GridViewAdapter gridviewAdapter;
    private TextView tvFolderName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery);
        tvFolderName = (TextView) findViewById(R.id.tv_gallery_name);
        gridview = (GridView) findViewById(R.id.gridview);
        position = getIntent().getIntExtra("position", 0);

        // set up the click to go to a details page
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int picposition, long id) {
                Intent i = new Intent(SubGalleryPage.this, DetailsPage.class);
                i.putExtra("position", position);
                i.putExtra("picposition", picposition);
                startActivity(i);
            }
        });

        tvFolderName.setText(GalleryPage.imageFolders.get(position).getFolderName() );
        gridviewAdapter = new GridViewAdapter(this, GalleryPage.imageFolders, position);
        gridview.setAdapter(gridviewAdapter);
    }
}
