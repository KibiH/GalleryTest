package com.kibisoftware.gallerytest;

import java.util.ArrayList;

/**
 * Created by kibi on 27/02/18.
 */

public class ImageFolder {
    String folderName;
    ArrayList<String> imageUrls;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<String> getimageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
