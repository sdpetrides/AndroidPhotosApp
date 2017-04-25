package com.softwaremeth.group53.android53;

import java.io.IOException;
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

    public void writeApp() throws IOException {

    }


}
