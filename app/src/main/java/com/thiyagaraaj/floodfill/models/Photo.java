package com.thiyagaraaj.floodfill.models;

import java.net.MalformedURLException;
import java.util.Map;

public class Photo {
    private long id;
    private long albumId;
    private String title;
    private String url;
    private String thumbnailUrl;

    public Photo(){
    }

    public long getId() {
        return id;
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
