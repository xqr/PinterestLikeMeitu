package com.dodola.model;

import java.io.Serializable;

public class VideoInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String thumbUrl;
    private String title;
    private String updateTime;
    private String url;
    private String source;
    private int playbackCount;
    public String getThumbUrl() {
        return thumbUrl;
    }
    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public int getPlaybackCount() {
        return playbackCount;
    }
    public void setPlaybackCount(int playbackCount) {
        this.playbackCount = playbackCount;
    }
    
    public int getWidth() {
        return 550;
    }

//    public void setWidth(int width) {
//        this.width = width;
//    }

    public int getHeight() {
        return 902;
    }

//  public void setHeight(int height) {
//      this.height = height;
//  }
}
