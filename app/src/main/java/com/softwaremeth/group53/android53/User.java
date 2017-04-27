package com.softwaremeth.group53.android53;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public ArrayList<Album> albums;
    public ArrayList<Photo> photos;

    public Album currentAlbum;
    public Photo currentPhoto;

    public int albumPos;
    public int photoPos;

    public String currentAlbumName;
    public String currentPhotoPath; // may not need

    public User() {
        this.albums = new ArrayList<Album>();
        this.photos = new ArrayList<Photo>();
        this.currentAlbum = null;
        this.currentPhoto = null;
        this.albumPos = -1;
        this.photoPos = -1;
        this.currentAlbumName = null;
        this.currentPhotoPath = null;
    }

    public String[] getAlbumsArray() {

        String[] albumsArray = new String[albums.size()];

        for (int i = 0; i < albumsArray.length; i++) {
            albumsArray[i] = albums.get(i).getAlbumName();
        }

        return albumsArray;
    }

    public void saveState(Context c) {

        FileOutputStream fos = null;
        try {
            fos = c.openFileOutput("user.dat", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User retrieveState(Context c) {

        FileInputStream fis = null;
        User u = null;
        try {
            fis = c.openFileInput("user.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            u = (User) ois.readObject();
            ois.close();
            fis.close();
            return u;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return u;
    }
}
