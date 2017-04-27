package com.softwaremeth.group53.android53;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private Album selectedAlbum;

    private String album_name;

    private int pos;

    private int selectedPos = -1;

    private GridView parent;

    public ImageAdapter(Context c, String album_name, int pos) {
        this.mContext = c;
        this.album_name = album_name;
        this.pos = pos;
        try {
            selectedAlbum = AlbumList.user.albums.get(pos);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return selectedAlbum.getNumPhotos();
    }

    public Object getItem(int position) {

        selectedPos = position;

        for (int i = 0; i < parent.getChildCount(); i++) {
            if (i == position) {
                parent.getChildAt(position).setBackgroundColor(Color.BLUE);
            } else {
                parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
        parent.getChildAt(position).setSelected(true);

        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i).isSelected()) {
                break;
            }
        }

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

        this.parent = (GridView) parent;

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
    }

    public void removePicture(int pos) {

        // remove photo from album
        selectedAlbum.removeAt(pos);

    }

}