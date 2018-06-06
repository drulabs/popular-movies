package org.drulabs.popularmovies.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.drulabs.popularmovies.R;
import org.drulabs.popularmovies.config.AppConstants;
import org.drulabs.popularmovies.data.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieVH> {

    private static final String TMDB_POSTER_BASE = AppConstants.TMDB_POSTER_BASE;

    private final List<Movie> movieList;

    private final MovieInteractionListener listener;

    MovieAdapter(@NonNull MovieInteractionListener listener) {
        this.movieList = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,
                parent, false);
        return new MovieVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {
        Movie movie = movieList.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    void append(@NonNull List<Movie> movies) {
        this.movieList.addAll(movies);
        notifyDataSetChanged();
    }

    void clearAndReload(@NonNull List<Movie> movies) {
        this.movieList.clear();
        this.movieList.addAll(movies);
        notifyDataSetChanged();
    }

    class MovieVH extends RecyclerView.ViewHolder {

        final ImageView posterImg;

        MovieVH(@NonNull View itemView) {
            super(itemView);
            posterImg = itemView.findViewById(R.id.img_movie_poster);
        }

        void bind(@NonNull Movie movie) {

            String posterUrl = TMDB_POSTER_BASE + movie.getPosterPath();

            Picasso.with(itemView.getContext())
                    .load(posterUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.mipmap.ic_launcher)
                    .into(posterImg);
            itemView.setOnClickListener(v -> listener.onClick(movie));
        }
    }

    interface MovieInteractionListener {
        void onClick(Movie movie);
    }
}
