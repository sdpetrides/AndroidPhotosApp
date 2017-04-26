package com.softwaremeth.group53.android53;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    public ArrayList<Album> albums;
    public ArrayList<Photo> photos;

    private static final long serialVersionUID = 1L;

    public User() {
        this.albums = new ArrayList<Album>();
        this.photos = new ArrayList<Photo>();
    }

    public void writeApp(Context c) throws IOException {
        FileOutputStream fos = c.openFileOutput("user.dat", Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
        fos.close();
    }

    public static User readApp(Context c) throws IOException, ClassNotFoundException {
        FileInputStream fis = c.openFileInput("user.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        User u = (User)ois.readObject();
        ois.close();
        fis.close();
        return u;
    }
}
