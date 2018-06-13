
package org.drulabs.popularmovies.data.models;

import java.util.List;
import com.google.gson.annotations.Expose;


public class VideoResult {

    @Expose
    private Long id;
    @Expose
    private List<Video> videos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

}
