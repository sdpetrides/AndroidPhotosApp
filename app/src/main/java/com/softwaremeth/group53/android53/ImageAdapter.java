package com.softwaremeth.group53.android53;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private Album selectedAlbum;

    public ImageAdapter(Context c, Album a) {
        this.mContext = c;
        try {
            selectedAlbum = a;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return selectedAlbum.getNumPhotos();
    }

    public Object getItem(int position) {

        return selectedAlbum.getPhotoAt(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public ImageView getImageView(int position) {
        return null;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        // create bitmap
        Bitmap bm = BitmapFactory.decodeFile(selectedAlbum.getPhotoAt(position).getPath());

        // set image bitmap
        imageView.setImageBitmap(bm);

        return imageView;
    }

    public void addPicture(String path) {

        // create photo
        Photo p = new Photo(path);

        // add photo to album
        selectedAlbum.addPhoto(p);
        AlbumList.user.allPhotos.addPhoto(p);
    }

    public void removePicture(int pos) {

        // remove photo from album
        selectedAlbum.removeAt(pos);
    }
}