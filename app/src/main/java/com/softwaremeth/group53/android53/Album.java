package com.softwaremeth.group53.android53;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {

    private String albumName;

    private ArrayList<Photo> photos;

    private static final long serialVersionUID = 1L;

    public Album(String album_name) {
        this.albumName = album_name;
        this.photos = new ArrayList<Photo>();
    }

    public String getAlbumName() {
        return albumName;
    }

    public Photo getPhotoAt(int position) {
        return photos.get(position);
    }

    public int getNumPhotos() {
        return photos.size();
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void addPhoto(Photo photo){
        photos.add(photo);
    }

    public String toString() {
        return albumName;
    }

    public void removeAt(int pos) {
        photos.remove(pos);
    }
}
