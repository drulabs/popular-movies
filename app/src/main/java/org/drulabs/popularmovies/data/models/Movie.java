package org.drulabs.popularmovies.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "moviesdb")
public class Movie {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")

    private String backdropPath;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("adult")
    private boolean isAdult;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("overview")
    private String overview;

    @ColumnInfo(name = "averageVote")
    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("vote_count")
    private long voteCount;

    @SerializedName("tagline")
    private String tagline;

    @SerializedName("status")
    private String status;

    @SerializedName("runtime")
    private int runtime;

    @SerializedName("revenue")
    private long revenue;

    @SerializedName("budget")
    private long budget;

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    private double popularity;

    @SerializedName("imdb_id")
    private String imdbId;

    @SerializedName("homepage")
    private String homepage;

    @ColumnInfo(name = "favorite")
    private boolean isFavorite;

    @ColumnInfo(name = "category")
    private int category;

//    public Movie(long id, String title, String posterPath, String backdropPath, String
//            originalTitle, boolean isAdult, String releaseDate, String overview, double
//            voteAverage, long voteCount, String tagline, String status, int runtime, long
//            revenue, long budget, double popularity, String imdbId, String homepage) {
//        this.id = id;
//        this.title = title;
//        this.posterPath = posterPath;
//        this.backdropPath = backdropPath;
//        this.originalTitle = originalTitle;
//        this.isAdult = isAdult;
//        this.releaseDate = releaseDate;
//        this.overview = overview;
//        this.voteAverage = voteAverage;
//        this.voteCount = voteCount;
//        this.tagline = tagline;
//        this.status = status;
//        this.runtime = runtime;
//        this.revenue = revenue;
//        this.budget = budget;
//        this.popularity = popularity;
//        this.imdbId = imdbId;
//        this.homepage = homepage;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return (new Gson()).toJson(this);
    }
}
