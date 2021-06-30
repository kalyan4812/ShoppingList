package com.saikalyandaroju.shoppinglist.data.remote.models;

import java.util.List;

public class ImageResponse {
    private List<ImageResult> hits;
    private int total;
    private int totalHits;

    public ImageResponse(List<ImageResult> hits, int total, int totalHits) {
        this.hits = hits;
        this.total = total;
        this.totalHits = totalHits;
    }

    public List<ImageResult> getHits() {
        return hits;
    }

    public void setHits(List<ImageResult> hits) {
        this.hits = hits;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }
}
